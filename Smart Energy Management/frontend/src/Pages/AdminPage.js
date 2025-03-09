import React, { useState, useEffect,useRef } from 'react';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import axios from 'axios';
import "../Style/AdminPage.css";
import { useNavigate } from 'react-router-dom';
import { Navigate } from "react-router-dom";
import {CompatClient, Stomp} from "@stomp/stompjs";

import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { tokenToString } from 'typescript';
export default function AdminPage() {
  const [messages,setMessages] = useState([]);
  const[message,setMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [typing,setTyping] = useState("");
  const [userList, setUserList] = useState([]); // Lista utilizatorilor
  const [activeTab, setActiveTab] = useState(""); // Utilizator activ


  const activeTabRef = useRef(activeTab);


  const [users, setUsers] = useState([]);
  const [editedUser, setEditedUser] = useState(null);
  const [devices, setDevices] = useState([]);
  const [editedDevice, setEditedDevice] = useState(null);
  const navigate = useNavigate();

  const [newUser, setNewUser] = useState({
    email: '',
    password: '',
    name: '',
    role: '',
  });

  const [newDevice, setNewDevice] = useState({
    description: '',
    address: '',
    consumption: '',
    maxHourlyConsumption:'',
    userId: ''
  });

  const handleLogout = () => {
    navigate('/Login');
    localStorage.removeItem("jwtToken");

  };

  const isAuthenticated = () => {
    const token = localStorage.getItem("jwtToken");
    return token !== null; // Returnează true doar dacă există token
  };
  
  const ProtectedRoute = ({ children }) => {
    if (!isAuthenticated()) {
      return <Navigate to="/Login" />; // Redirecționează la Login dacă nu e autentificat
    }
    return children; // Permite accesul la pagină
  };
  //export default ProtectedRoute;

// CHATT

useEffect(() => {
  activeTabRef.current = activeTab;
}, [activeTab]);

// Conectare WebSocket și gestionare mesaje
useEffect(() => {
  // Crează instanța SockJS pentru WebSocket
  const socket = new SockJS(`${process.env.REACT_APP_BACKEND5_URL}/ws`);

  // Crează clientul STOMP folosind SockJS
  const stomp = Stomp.over(socket);

  stomp.connect({}, () => {
    console.log("Conectat la WebSocket");

    // Subscriere la mesaje generale
    stomp.subscribe("/chat/message/admin", (msg) => {
      const receivedMessage = JSON.parse(msg.body);

      setMessages((prevMessages) => [...prevMessages, receivedMessage]);

      // Adaugă utilizatorul în lista de utilizatori dacă nu există deja
      setUserList((prevUsers) =>
        prevUsers.includes(receivedMessage.sender)
          ? prevUsers
          : [...prevUsers, receivedMessage.sender]
      );

      // Afișează notificare dacă mesajul vine de la un utilizator neactiv
      if (receivedMessage.sender !== activeTabRef.current) {
        alert(`Mesaj nou de la ${receivedMessage.sender}`);
      }
    });

    // Subscriere la notificări "typing"
    stomp.subscribe("/chat/admin/typing", (msg) => {
      console.log("Notif primit:", msg.body);
      const notification = JSON.parse(msg.body);
      alert(`Utilizatorul ${notification.user} tasteaza`);
      setTyping(true);
    });

    // stomp.subscribe("/chat/admin/read",(msg) =>{
    //   const readNotification = JSON.parse(msg.body);

    //   // Afișează notificarea pentru admin (exemplu simplu cu alertă)
    //   alert(`Utilizatorul ${receivedMessage.sender} a citit mesajul cu ID ${readNotification.messageId}`);
    // });


    setStompClient(stomp); // Salvează conexiunea pentru utilizare ulterioară
  });

  stomp.onStompError = (error) => {
    console.error("Eroare STOMP:", error);
  };

  return () => {
    // Deconectare la WebSocket și dezactivare STOMP
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("Deconectat de la WebSocket");
      });
    }
  };
}, []);

// Schimbare chat activ
const switchChat = (userId) => {
  setActiveTab(userId);
};

// Trimitere mesaj către utilizatorul activ
const sendMessageToUser = () => {
  if (!activeTab) {
    alert("Nu ai selectat niciun utilizator activ!");
    return;
  }
  if (message.trim() !== "") {
    const messageToSend = {
      receiver: activeTab,
      sender: "Admin",
      content: message,
      timestamp: Date.now(),
    };

    // Publică mesajul către backend
    if (stompClient) {
      console.log("nume"+activeTab);
      stompClient.send(
        `/chat/${activeTab}/message`, // Destinația mesajului
        {},
        JSON.stringify(messageToSend)
      );
    }

    // Adaugă mesajul în lista locală
    setMessages((prev) => [...prev, messageToSend]);
    setMessage(""); // Golește câmpul de input
  }
};

 
const handleTyping = () => {
  if (stompClient) {
    const typingNotification = {
      user: "Admin",
      isTyping: true,
    };
    stompClient.publish({
      destination: `/chat/${activeTab}/typing`,
      body: JSON.stringify(typingNotification),
    });  
    // Oprire notificare "typing" după 2 secunde
   
  }
};





// CHAT
  useEffect(() => {
    fetchUsers();
    fetchDevices();
  }, []);

  const getAuthHeader = () => {
    const token = localStorage.getItem("jwtToken");
    console.log("Authorized!!");
    console.log("Token valid: "+token);

    return {
      headers: {
        Authorization: `Bearer ${token}`, // Adaugă token-ul în header
        "Content-Type": "application/json",
      },
    };
  };
  

  const fetchUsers = async () => {
    try {
      const token = localStorage.getItem("jwtToken"); // Ia token-ul JWT salvat în localStorage
  
      if (!token) {
        console.error("JWT Token is missing!");
        return; // Oprește execuția dacă token-ul nu există
      }

      const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decodifică doar payload-ul
      const userRole = decodedToken.role; // Presupunând că rolul este salvat în payload cu cheia 'role'
        console.log("ROl: "+userRole);
      if (userRole !== "Admin") {
        console.error("Access denied: You do not have the necessary role to fetch users!");
        return;
      }
      console.log("Authorized with token:" +token);
      const response = await axios.get(
        `${process.env.REACT_APP_BACKEND1_URL}/User/GetAllUsers`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Adaugă token-ul în header
          },
        }

      );
  
      setUsers(response.data); // Setează datele utilizatorilor
    } catch (error) {
      console.error("Error fetching users:", error); // Loghează erorile
    }
  };
  
  const fetchDevices = async () => {
    try {
      const token = localStorage.getItem("jwtToken"); // Ia token-ul JWT salvat în localStorage
      if (!token) {
        console.error("JWT Token is missing!");
        return; // Oprește execuția dacă token-ul nu există
      }
  
      const response = await axios.get(`${process.env.REACT_APP_BACKEND2_URL}/device-service/GetAllDevices`,   {
        headers: {
          Authorization: `Bearer ${token}`, // Adaugă token-ul în header
        },
      }

    );
      setDevices(response.data);
    } catch (error) {
      console.error('Error fetching devices:', error);
    }
  };

  const handleInputChange = (event, key) => {
    if (editedUser && editedUser.idUser === 'new') {
      setNewUser((prevNewUser) => ({
        ...prevNewUser,
        [key]: event.target.value,
      }));
    } else {
      setEditedUser((prevEditedUser) => ({
        ...prevEditedUser,
        [key]: event.target.value,
      }));
    }
  };

  const handleInputChangeDevice = (event, key) => {
    if (editedDevice && editedDevice.id === 'new') {
      setNewDevice((prevNewDevice) => ({
        ...prevNewDevice,
        [key]: event.target.value,
      }));
    } else {
      setEditedDevice((prevEditedDevice) => ({
        ...prevEditedDevice,
        [key]: event.target.value,
      }));
    }
  };

  const handleEdit = (user) => setEditedUser({ ...user });
  const handleEditDevice = (device) => setEditedDevice({ ...device });

  const handleSave = async () => {
    try {
      await axios.post(`${process.env.REACT_APP_BACKEND1_URL}/User/Update`, editedUser);
      fetchUsers();
      setEditedUser(null);
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  const handleSaveDevice = async () => {
    try {
      await axios.post(`${process.env.REACT_APP_BACKEND2_URL}/device-service/UpdateDevice`, editedDevice);
      fetchDevices();
     // console.log("Esti autorizat!"+token);
      setNewDevice({ description: '', address: '', consumption: '',maxHourlyConsumption:'', userId: '' }); // Resetează câmpurile

      setEditedDevice(null);
    } catch (error) {
      console.error('Error updating device:', error);
    }
  };

  const handleDelete = (user) => {
    axios.post(`${process.env.REACT_APP_BACKEND1_URL}/User/Delete`, user)
      .then(() => fetchUsers())
      .catch((error) => console.error('Error deleting user:', error));
  };

  const handleDeleteDevice = (device) => {
    axios.post(`${process.env.REACT_APP_BACKEND2_URL}/device-service/DeleteDevice`, device)
      .then(() => fetchDevices())
      .catch((error) => console.error('Error deleting device:', error));
      console.log("Esti autorizat!"+tokenToString);

  };

  const handleInsert = () => {
    setNewUser({
      email: '',
      password: '',
      name: '',
      role: '',
    });
    setEditedUser({ idUser: 'new' });
  };

  const handleInsertDevice = () => {
    setNewDevice({
      description: '',
      address: '',
      consumption: '',
      maxHourlyConsumption:'',
      userId: ''
    });
    setEditedDevice({ id: 'new' });
  };

  const handleSubmitNewUser = () => {
    // Verifică dacă toate câmpurile sunt completate
    if (Object.values(newUser).every(value => value !== '')) {
      axios
        .post(
          `${process.env.REACT_APP_BACKEND1_URL}/User/Insert`,
          newUser,
          getAuthHeader() // Adaugă token-ul în request
        )
        .then(() => {
          fetchUsers(); // Actualizează lista de utilizatori
          setEditedUser(null);
        })
        .catch(error => {
          console.error('Error inserting user:', error);
          if (error.response && error.response.status === 401) {
            alert("Nu ești autorizat! Reautentifică-te.");
            navigate("/Login"); // Redirecționează dacă token-ul este invalid
          }
        });
    } else {
      alert('Completează toate câmpurile pentru noul utilizator!');
    }
  };
  const handleSaveUser = async () => {
    try {
      await axios.post(
        `${process.env.REACT_APP_BACKEND1_URL}/User/Update`,
        editedUser,
        getAuthHeader() // Adaugă token-ul în request
      );
      fetchUsers(); // Reîmprospătează lista de utilizatori
      setEditedUser(null); // Ieși din modul de editare
    } catch (error) {
      console.error('Error updating user:', error);
      if (error.response && error.response.status === 401) {
        alert("Nu ești autorizat! Reautentifică-te.");
        navigate("/Login"); // Redirecționează dacă token-ul este invalid
      }
    }
  };
  
  const handleEditUser = (user) => {
    setEditedUser({ ...user });
  };
  
  

  const handleDeleteUser = async (user) => {
    try {
      await axios.post(
        `${process.env.REACT_APP_BACKEND1_URL}/User/Delete`,
        user,
        getAuthHeader()
      );
      fetchUsers();
    } catch (error) {    const message = error.response?.data || 'A apărut o eroare.';
        alert(message); // Afișează mesajul de alertă
    }
};

  const handleSubmitNewDevice = () => {
    if (Object.values(newDevice).every(value => value !== '')) {
        getAuthHeader()
        axios.post(`${process.env.REACT_APP_BACKEND2_URL}/device-service/InsertDevice`, newDevice)
      .then(() => {
        fetchDevices();
       // getAuthHeader() ;// Adaugă token-ul în request
       const token = localStorage.getItem("jwtToken");
       console.log("Authorized!!");
       console.log("Token valid: "+token);
      setNewDevice({ description: '', address: '', consumption: '', userId: '' }); // Resetează câmpurile

        setEditedDevice(null);
      })
   
        .catch(error => console.error('Error inserting device:', error));
    } else {
      alert('Complete all fields for the new device!');
    }
  };
  const renderInputField = (user, key) => {
    if (editedUser && editedUser.id === user.id) {
      return (
        <TextField
          value={editedUser[key]}
          onChange={(event) => handleInputChange(event, key)}
          variant="outlined"
          size="small"
        />
      );
    }
    return user[key];
  };
  
  const renderInputFieldDevice = (device, key) => {
    if (editedDevice && editedDevice.id === device.id) {
      return (
        <TextField
          value={editedDevice[key]}
          onChange={(event) => handleInputChangeDevice(event, key)}
          variant="outlined"
          size="small"
        />
      );
    }
    return device[key];
  };
  

  return (
    <div className="admin-container">
      <Button variant="contained" onClick={handleLogout}>Deconectare</Button>
      
      <h1>Utilizatori</h1>
      <Button variant="contained" color="primary" onClick={handleInsert}>Adauga utilizator</Button>
      
      {/* Form for Adding New User */}
      {editedUser && editedUser.idUser === 'new' && (
        <div className="new-user-form">
          <TextField
            label="Email"
            value={newUser.email}
            sx={{backgroundColor:'white'}}

            onChange={(e) => handleInputChange(e, 'email')}
          />
          <TextField
            label="Password"
            type="password"
            value={newUser.password}
            sx={{backgroundColor:'white'}}

            onChange={(e) => handleInputChange(e, 'password')}
          />
          <TextField
            label="Name"
            value={newUser.name}
            sx={{backgroundColor:'white'}}

            onChange={(e) => handleInputChange(e, 'name')}
          />
          <TextField
            label="Role"
            value={newUser.role}
            sx={{backgroundColor:'white'}}

            onChange={(e) => handleInputChange(e, 'role')}
          />
        
          <Button variant="contained" color="primary" onClick={handleSubmitNewUser}>
            Submit
          </Button>
        </div>
      )} 
      {/* Users Table */}
      <table className="user-table">
  <thead>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Password</th>
      <th>Role</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
    {users.map(user => (
      <tr key={user.id}>
        <td>{user.id}</td>
        <td>{renderInputField(user, 'name')}</td>
        <td>{renderInputField(user, 'email')}</td>
        <td>{renderInputField(user, 'password')}</td>

        <td>{renderInputField(user, 'role')}</td>
        <td className="user-actions">
          {editedUser && editedUser.idUser === user.idUser ? (
            <>
              <Button className="button-primary" onClick={handleSaveUser}>
                Save
              </Button>
              <Button onClick={() => setEditedUser(null)}>
                Cancel
              </Button>
            </>
          ) : (
            <>
              <Button onClick={() => handleEditUser(user)}>
                Edit
              </Button>
              <Button className="button-secondary" onClick={() => handleDeleteUser(user)}>
                Delete
              </Button>
            </>
          )}
        </td>
      </tr>
    ))}
  </tbody>
</table>
<h1>Dispozitive</h1>
  <Button variant="contained" color="primary" onClick={() => setEditedDevice({ id: 'new' })}>Adauga dispozitiv</Button>

      {/* Devices Table */}
      {editedDevice && editedDevice.id === 'new' && (
  <div className="new-device-form">
    <TextField
      label="Descriere"
      value={newDevice.description}
      sx={{ backgroundColor: 'white' }}
      onChange={(e) => handleInputChangeDevice(e, 'description')}
    />
    <TextField
      label="Adresa"
      value={newDevice.address}
      sx={{ backgroundColor: 'white' }}
      onChange={(e) => handleInputChangeDevice(e, 'address')}
    />
    <TextField
      label="Consum"
      type="number"
      value={newDevice.consumption}
      sx={{ backgroundColor: 'white' }}
      onChange={(e) => handleInputChangeDevice(e, 'consumption')}
    />
      <TextField
      label="Max Consum"
      type="number"
      value={newDevice.maxHourlyConsumption}
      sx={{ backgroundColor: 'white' }}
      onChange={(e) => handleInputChangeDevice(e, 'maxHourlyConsumption')}
    />
  
    <TextField
      label="ID Utilizator"
      value={newDevice.userId}
      sx={{ backgroundColor: 'white' }}
      onChange={(e) => handleInputChangeDevice(e, 'userId')}
    />

    <Button variant="contained" color="primary" onClick={handleSubmitNewDevice}>
      Submit
    </Button>
  </div>
)}
 
      <table className="device-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Descriere</th>
            <th>Adresa</th>
            <th>Consum</th>
            <th>ID Utilizator</th>
            <th>Actiuni</th>
          </tr>
        </thead>
        <tbody>
          {devices.map(device => (
            <tr key={device.id}>
              <td>{device.id}</td>
              <td>{renderInputFieldDevice(device, 'description')}</td>
              <td>{renderInputFieldDevice(device, 'address')}</td>
              <td>{renderInputFieldDevice(device, 'consumption')}</td>
              <td>{renderInputFieldDevice(device, 'userId')}</td>
              <td className="device-actions">
                {editedDevice && editedDevice.id === device.id ? (
                  <>
                    <Button onClick={handleSaveDevice}>Save</Button>
                    <Button onClick={() => setEditedDevice(null)}>Cancel</Button>
                  </>
                ) : (
                  <>
                    <Button onClick={() => handleEditDevice(device)}>Edit</Button>
                    <Button onClick={() => handleDeleteDevice(device)}>Delete</Button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="admin-chat-container">
      {/* Lista utilizatorilor */}
      <div className="user-list">
        <h3>Utilizatori</h3>
        {userList.map((user) => (
          <div
            key={user}
            className={`user-tab ${activeTab === user ? "active" : ""}`}
            onClick={() => switchChat(user)}
          >
            {user}
          </div>
        ))}
      </div>

      {/* Fereastra de chat */}
      <div className="chat-window">
        <h3>Chat cu {activeTab || "..."}</h3>
        <div className="message-list">
          {messages
            .filter((msg) => msg.sender === activeTab || msg.receiver === activeTab)
            .map((msg, idx) => (
              <div
                key={idx}
                className={`message ${
                  msg.sender === "Admin" ? "sent" : "received"
                }`}
              >
                <p>{msg.content}</p>
                <small>
                  {msg.sender} - {new Date(msg.timestamp).toLocaleTimeString()}
                </small>
              </div>
            ))}
        </div>

        {/* Input pentru mesaj */}
        <div className="message-input">
          <input
            type="text"
            onKeyUp={handleTyping}
            placeholder="Scrie un mesaj..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
          <button onClick={sendMessageToUser}>Trimite</button>
        </div>
      </div>
    </div>

    </div>
  );
}