import React from "react";
import { Navigate } from "react-router-dom";
import { useNavigate } from 'react-router-dom';

const isAuthenticated = () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) return { isValid: false, role: null }; // Nu există token

  try {
    const payload = JSON.parse(atob(token.split(".")[1])); // Decodifică token-ul
    const exp = payload.exp * 1000;

    if (Date.now() > exp) {
      localStorage.removeItem("jwtToken"); // Șterge token-ul expirat
      return { isValid: false, role: null };
    }

    return { isValid: true, role: payload.role }; // Returnează rolul
  } catch (error) {
    return { isValid: false, role: null };
  }
};

const ProtectedRoute = ({ children, requiredRole }) => {
  const auth = isAuthenticated();
  const navigate = useNavigate();

  // Verifică dacă este autentificat
  if (!auth.isValid) {
    return <Navigate to="/Login" />;
  }

  // Verifică dacă rolul nu este autorizat
  if (requiredRole && auth.role !== requiredRole) {
    return (
      <div style={{ color: "red", textAlign: "center", marginTop: "20px" }}>
        <h1>Nu ești autorizat să accesezi această pagină!</h1>
        <button onClick={() => (navigate("/Platform"))}>
          Înapoi la Platformă
        </button>
      </div>
    );
  }

  return children; // Dacă e autorizat, afișează conținutul
};

export default ProtectedRoute;
