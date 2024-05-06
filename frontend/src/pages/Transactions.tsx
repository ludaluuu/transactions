import React, { useState, useEffect } from 'react';
import DatePicker  from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

interface AppResponse {
  date: Date;
  recipientId?: string;
  description: string;
  amount: number;
  classification: string
};

interface UpdatedClassification {
  recipientId: string;
  classification: string;
}

function Transactions() {
  const [response, setResponse] = useState<AppResponse[]>([]);
  const [startDate, setStartDate] = useState<any>(null);
  const [endDate, setEndDate] = useState<any>(null);
  const [error, setError] = useState<string>();
  const [isEdit, setEdit] = useState<boolean>(false);
  const [updatedClassification, setUpdatedClassification] = useState<UpdatedClassification>({
    recipientId: "0",
    classification: ""
  });

  async function fetchAllTransactions(): Promise<AppResponse[]> {
    const response = await fetch("/transactions/all");
    const transactions = await response.json();
    return transactions;
  }

  async function fetchTransactionsInInterval() {
    try {
      const fromDate = new Date(startDate).toLocaleDateString().substring(0,10);
      const toDate = new Date(endDate).toISOString().substring(0,10);
      const response = await fetch(`/transactions/date?fromDate=${fromDate}&toDate=${toDate}`);
      const transactions = await response.json();
      if (!response.ok) {
        alert(JSON.stringify(transactions));
      } else {
        setResponse(transactions);
        setEdit(false);
      }
    } catch (error) {
      setError("Could not connect to server")
    }
  }

  async function updateClassification() {
    const requestOpts = {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({
        classification: updatedClassification.classification,
        recipientId: updatedClassification.recipientId
      })
    };
    try {
      const response = await fetch("/transactions/update", requestOpts);
      const res = await response.json();
      if (!response.ok) {
        alert(JSON.stringify(res));
      } else {
        console.log("Updated classification");
      }
    } catch(error) {
      setError("Could not connect to server")
    }
  }

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await fetchAllTransactions();
        setResponse(data);
      } catch (error) {
        setError("Could not connect to server");
      }
    }
    fetchData();
  }, []);

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await fetchAllTransactions();
        setResponse(data);
      } catch (error) {
        setError("Could not connect to server");
      }
    }
    fetchData();
  }, [updatedClassification]);

  function handleSubmit() {
    fetchTransactionsInInterval();
  }

  function reclassify(e: React.ChangeEvent<HTMLSelectElement>) {
    setUpdatedClassification({
      recipientId: e.target.name,
      classification: e.target.value
    });
  }

  function handleEditClick() {
    setEdit(true);
  }

  function handleEditSubmit() {
    setResponse(response.map((transaction) => {
      if (transaction.recipientId === updatedClassification?.recipientId) {
        return {...transaction, classification: updatedClassification?.classification};
      } else{
        return transaction;
      }
    }));
    updateClassification();
    setEdit(false);
  }

  if (error) {
    return (
      <>
        <p>{error}</p>
      </>
    )
  }
 
  return (
    <>
      <h1>Outgoing transactions</h1>
      <div>
        <p>Filter on dates</p>
        <DatePicker
          dateFormat="yyyy-MM-dd"
          selectsStart
          isClearable
          placeholderText="Select date from"
          selected={startDate}
          onChange={(date) => setStartDate(date)}
          startDate={startDate}
        />
        &nbsp;
        <DatePicker
          dateFormat="yyyy-MM-dd"
          selectsEnd
          isClearable
          placeholderText="Select date to"
          selected={endDate}
          onChange={(date) => setEndDate(date)}
          endDate={endDate}
          startDate={startDate}
          minDate={startDate}
        />
        &nbsp;
        <input type="button" value="Filter" onClick={handleSubmit}></input>
      </div>
      &nbsp;
      <div>
        <table border={1}>
          <thead>
            <tr>
              <th><strong>Date</strong></th>
              <th><strong>Description</strong></th>
              <th><strong>Amount</strong></th>
              <th><strong>Classification</strong></th>
              <th><strong>Edit classification</strong></th>
            </tr>
          </thead>
          <tbody>
            {
              isEdit === false ?
              response.map((transaction, idx) => (
                <tr key={idx}>
                  <td>{transaction.date.valueOf()}</td>
                  <td>{transaction.description}</td>
                  <td>{transaction.amount}</td>
                  <td>{transaction.classification}</td>
                  <td>
                    <button onClick={handleEditClick}>Edit</button>
                  </td>
                </tr>
              ))
              : 
              response.map((transaction, idx) => (
                <tr key={idx}>
                  <td>{transaction.date.valueOf()}</td>
                  <td>{transaction.description}</td>
                  <td>{transaction.amount}</td>
                  <td>
                    <select onChange={reclassify} name={transaction.recipientId}>
                      <option>{transaction.classification}</option>
                      <option>HOUSEHOLD</option>
                      <option>TRANSPORT</option>
                      <option>FOOD</option>
                      <option>ENTARTAINMENT</option>
                      <option>UNKONWN</option>
                    </select>
                  </td>
                  <td>
                    <button onClick={handleEditSubmit}>Update</button>
                  </td>
                </tr>
              ))
            }
          </tbody>
        </table>
      </div>
    </>
  );
}

export default Transactions;