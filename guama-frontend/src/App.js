import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TransactionList from './components/TransactionList';
import TransactionCreate from './components/CreateTransaction';
import UpdateTransaction from './components/UpdateTransaction';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<TransactionList />} />
        <Route path="/agregar" element={<TransactionCreate />} />
        <Route path="/edit/:id" element={<UpdateTransaction />} />
      </Routes>
    </Router>
  );
}

export default App;
