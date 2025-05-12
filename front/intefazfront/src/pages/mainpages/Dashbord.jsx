import { useState, useEffect } from 'react';
import axiosInstance from '../../config/axios';

// Configurar axios por defecto
axiosInstance.defaults.headers.common['Content-Type'] = 'application/json';
axiosInstance.defaults.headers.common['Accept'] = 'application/json';

function Dashboard() {
  const [users, setUsers] = useState([]);
  const [editingUser, setEditingUser] = useState(null);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axiosInstance.get('/users');
      setUsers(response.data);
      setError(null); // Limpiar error si la petición es exitosa
    } catch (err) {
      console.error('Error completo:', err);
      const errorMessage = err.response?.data?.message || err.message;
      setError(`Error al cargar usuarios: ${errorMessage}`);
      setUsers([]); // Limpiar usuarios en caso de error
    }
  };

  const handleUpdateUser = async (e) => {
    e.preventDefault();
    try {
      await axiosInstance.put(`/users/${editingUser.id}`, editingUser);
      setSuccess('Usuario actualizado exitosamente');
      setError(null);
      setEditingUser(null);
      fetchUsers();
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message;
      setError(`Error al actualizar usuario: ${errorMessage}`);
      setSuccess(null);
    }
  };

  const handleDeleteUser = async (userId) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      try {
        await axiosInstance.delete(`/users/${userId}`);
        setSuccess('Usuario eliminado exitosamente');
        setError(null);
        fetchUsers();
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message;
        setError(`Error al eliminar usuario: ${errorMessage}`);
        setSuccess(null);
      }
    }
  };

  const handleFormDataChange = (field, value) => {
    if (editingUser) {
      setEditingUser(prev => ({
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

export default Dashboard;
