import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const AdminRoute = () => {
  const isAdmin = true; // Replace with auth & role check
  return isAdmin ? <Outlet /> : <Navigate to="/" />;
};

export default AdminRoute;
