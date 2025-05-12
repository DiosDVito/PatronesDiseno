import { NavLink } from "react-router-dom";
import "./Navbar.css"; // Importa el CSS específico para Navbar

function Navbar() {
  return (
    <nav className="navbar">
      <div className="nav-container">
        <NavLink to="/" className="nav-link">Crear Usuario</NavLink>
        <NavLink to="/dashboard" className="nav-link">Gestión de Usuarios</NavLink>
      </div>
    </nav>
  );
}

export default Navbar;