import React from 'react';

const Input = ({ label, type = 'text', name, register, errors, ...rest }) => {
  return (
    <div className="mb-4">
      {label && <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>}
      <input
        type={type}
        {...(register ? register(name) : {})}
        className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-indigo-500"
        {...rest}
      />
      {errors && errors[name] && <p className="text-red-500 text-xs mt-1">{errors[name].message}</p>}
    </div>
  );
};

export default Input;
