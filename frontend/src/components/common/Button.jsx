import React from 'react';

const Button = ({ children, type = 'button', onClick, className = '' }) => {
  return (
    <button
      type={type}
      onClick={onClick}
      className={`px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 transition ${className}`}
    >
      {children}
    </button>
  );
};

export default Button;
