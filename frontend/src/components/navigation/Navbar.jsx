import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
      <Link to="/" className="text-xl font-bold text-indigo-600">Smart Shop</Link>
      <div className="flex gap-4">
        <Link to="/products" className="text-gray-600 hover:text-indigo-600">Products</Link>
        <Link to="/cart" className="text-gray-600 hover:text-indigo-600">Cart</Link>
        <Link to="/login" className="text-gray-600 hover:text-indigo-600">Login</Link>
      </div>
    </nav>
  );
};

export default Navbar;
