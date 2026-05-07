import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = () => {
  const isAuth = true; // Replace with auth hook
  return isAuth ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;
