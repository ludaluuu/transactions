import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Transactions from './pages/Transactions';
import './App.css';


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Transactions />}></Route>
          </Routes>
        </BrowserRouter>
      </header>
    </div>
  );
}

export default App;
