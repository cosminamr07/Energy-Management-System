import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';
import '../Style/PlatformStyle.css';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';

export default function Simulator() {
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [isSimulating, setIsSimulating] = useState(false);

  useEffect(() => {
    // Obține lista dispozitivelor
    axios
      .get(`${process.env.REACT_APP_BACKEND2_URL}/device-service/GetAllDevices`)
      .then((response) => {
        setDevices(response.data);
      })
      .catch((error) => {
        console.error('Eroare la obținerea dispozitivelor:', error);
      });
  }, []);

  const handleStartSimulation = () => {
    if (!selectedDevice) return;

    axios
      .post(`${process.env.REACT_APP_BACKEND4_URL}/simulator/setDeviceId`, null, {
        params: { deviceId: selectedDevice },
      })
      .then(() => {
        setIsSimulating(true);
        console.log(`Simularea a început pentru dispozitivul: ${selectedDevice}`);
      })
      .catch((error) => {
        console.error('Eroare la pornirea simulării:', error);
      });
  };

  const handleStopSimulation = () => {
    setIsSimulating(false);
    console.log('Simularea a fost oprită.');
  };

  return (
    <div className="platform-container">
      <h2>Dispozitive asociate:</h2>
      <TableContainer component={Paper}>
        <Table aria-label="device table">
          <TableHead>
            <TableRow>
              <TableCell>Selecție</TableCell>
              <TableCell>ID Dispozitiv</TableCell>
              <TableCell align="right">Descriere</TableCell>
              <TableCell align="right">Adresă</TableCell>
              <TableCell align="right">Consum</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {devices.map((device) => (
              <TableRow key={device.id}>
                <TableCell>
                  <RadioGroup
                    value={selectedDevice}
                    onChange={() => setSelectedDevice(device.id)}
                  >
                    <FormControlLabel
                      value={device.id}
                      control={<Radio />}
                      label=""
                    />
                  </RadioGroup>
                </TableCell>
                <TableCell>{device.id}</TableCell>
                <TableCell align="right">{device.description}</TableCell>
                <TableCell align="right">{device.address}</TableCell>
                <TableCell align="right">{device.consumption}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Butoane pentru simulare */}
      <div style={{ marginTop: '20px' }}>
        <Button
          variant="contained"
          color="primary"
          onClick={handleStartSimulation}
          disabled={!selectedDevice || isSimulating}
        >
          Start Simulare
        </Button>
        <Button
          variant="contained"
          color="secondary"
          onClick={handleStopSimulation}
          disabled={!isSimulating}
          style={{ marginLeft: '10px' }}
        >
          Stop Simulare
        </Button>
      </div>
    </div>
  );
}
