import { useState, useEffect } from 'react';
import axios from 'axios';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({
    email: '',
    password: '',
    role: 'guest',
    formData: {}
  });
  const [editingUser, setEditingUser] = useState(null);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  // Fetch users on component mount
  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/users');
      setUsers(response.data);
    } catch (err) {
      setError('Error al cargar usuarios: ' + err.message);
    }
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/users', newUser);
      setSuccess('Usuario creado exitosamente');
      setNewUser({ email: '', password: '', role: 'guest', formData: {} });
      fetchUsers();
    } catch (err) {
      setError('Error al crear usuario: ' + err.message);
    }
  };

  const handleUpdateUser = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`http://localhost:8080/users/${editingUser.id}`, editingUser);
      setSuccess('Usuario actualizado exitosamente');
      setEditingUser(null);
      fetchUsers();
    } catch (err) {
      setError('Error al actualizar usuario: ' + err.message);
    }
  };

  const handleDeleteUser = async (userId) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      try {
        await axios.delete(`http://localhost:8080/users/${userId}`);
        setSuccess('Usuario eliminado exitosamente');
        fetchUsers();
      } catch (err) {
        setError('Error al eliminar usuario: ' + err.message);
      }
    }
  };

  const handleFormDataChange = (field, value) => {
    if (editingUser) {
      setEditingUser(prev => ({
        ...prev,
        formData: { ...prev.formData, [field]: value }
      }));
    } else {
      setNewUser(prev => ({
        ...prev,
        formData: { ...prev.formData, [field]: value }
      }));
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h2 className="text-2xl font-bold mb-6">Gestión de Usuarios</h2>
      
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      {success && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          {success}
        </div>
      )}

      {/* Formulario de creación de usuario */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h3 className="text-xl font-semibold mb-4">Crear Nuevo Usuario</h3>
        <form onSubmit={handleCreateUser} className="space-y-4">
          <div>
            <label className="block mb-1">Email:</label>
            <input
              type="email"
              value={newUser.email}
              onChange={(e) => setNewUser(prev => ({ ...prev, email: e.target.value }))}
              className="w-full border p-2 rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Contraseña:</label>
            <input
              type="password"
              value={newUser.password}
              onChange={(e) => setNewUser(prev => ({ ...prev, password: e.target.value }))}
              className="w-full border p-2 rounded"
              required
            />
          </div>
          <div>
            <label className="block mb-1">Rol:</label>
            <select
              value={newUser.role}
              onChange={(e) => setNewUser(prev => ({ ...prev, role: e.target.value }))}
              className="w-full border p-2 rounded"
            >
              <option value="guest">Guest</option>
              <option value="admin">Admin</option>
            </select>
          </div>
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Crear Usuario
          </button>
        </form>
      </div>

      {/* Lista de usuarios */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h3 className="text-xl font-semibold mb-4">Usuarios Existentes</h3>
        <div className="space-y-4">
          {users.map(user => (
            <div key={user.id} className="border p-4 rounded">
              {editingUser?.id === user.id ? (
                <form onSubmit={handleUpdateUser} className="space-y-4">
                  <div>
                    <label className="block mb-1">Email:</label>
                    <input
                      type="email"
                      value={editingUser.email}
                      onChange={(e) => setEditingUser(prev => ({ ...prev, email: e.target.value }))}
                      className="w-full border p-2 rounded"
                      required
                    />
                  </div>
                  <div>
                    <label className="block mb-1">Rol:</label>
                    <select
                      value={editingUser.role}
                      onChange={(e) => setEditingUser(prev => ({ ...prev, role: e.target.value }))}
                      className="w-full border p-2 rounded"
                    >
                      <option value="guest">Guest</option>
                      <option value="admin">Admin</option>
                    </select>
                  </div>
                  {/* Campos dinámicos según el rol */}
                  {editingUser.role === 'admin' ? (
                    <>
                      <div>
                        <label className="block mb-1">Username:</label>
                        <input
                          type="text"
                          value={editingUser.formData.username || ''}
                          onChange={(e) => handleFormDataChange('username', e.target.value)}
                          className="w-full border p-2 rounded"
                        />
                      </div>
                      <div>
                        <label className="block mb-1">Admin Key:</label>
                        <input
                          type="password"
                          value={editingUser.formData.adminKey || ''}
                          onChange={(e) => handleFormDataChange('adminKey', e.target.value)}
                          className="w-full border p-2 rounded"
                        />
                      </div>
                    </>
                  ) : (
                    <>
                      <div>
                        <label className="block mb-1">Nickname:</label>
                        <input
                          type="text"
                          value={editingUser.formData.nickname || ''}
                          onChange={(e) => handleFormDataChange('nickname', e.target.value)}
                          className="w-full border p-2 rounded"
                        />
                      </div>
                      <div>
                        <label className="block mb-1">Interests:</label>
                        <input
                          type="text"
                          value={editingUser.formData.interests || ''}
                          onChange={(e) => handleFormDataChange('interests', e.target.value)}
                          className="w-full border p-2 rounded"
                        />
                      </div>
                    </>
                  )}
                  <div className="flex gap-2">
                    <button
                      type="submit"
                      className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                    >
                      Guardar
                    </button>
                    <button
                      type="button"
                      onClick={() => setEditingUser(null)}
                      className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                    >
                      Cancelar
                    </button>
                  </div>
                </form>
              ) : (
                <div>
                  <p><strong>Email:</strong> {user.email}</p>
                  <p><strong>Rol:</strong> {user.role}</p>
                  {user.role === 'admin' ? (
                    <>
                      <p><strong>Username:</strong> {user.formData?.username}</p>
                    </>
                  ) : (
                    <>
                      <p><strong>Nickname:</strong> {user.formData?.nickname}</p>
                      <p><strong>Interests:</strong> {user.formData?.interests}</p>
                    </>
                  )}
                  <div className="mt-2 flex gap-2">
                    <button
                      onClick={() => setEditingUser(user)}
                      className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                    >
                      Editar
                    </button>
                    <button
                      onClick={() => handleDeleteUser(user.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                    >
                      Eliminar
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default UserManagement; 