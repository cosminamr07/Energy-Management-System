import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import backgroundImg from '../images/background.jpg'; 


function Copyright(props) {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        Your Website
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}


const defaultTheme = createTheme();

export default function SignIn() {
  const [email,setEmail] = React.useState("");//valoarea default
  const [password,setPassword] = React.useState("");
  const navigate = useNavigate();


  const handleSubmit = (event) => {
    if (!email || !password) {
      alert('Te rugăm să completezi toate câmpurile.');
      return;
    }
  
    event.preventDefault();
    axios
      .post(
        `${process.env.REACT_APP_BACKEND1_URL}/User/CheckLogin`,
        {
          email: email,
          password: password,
        },
        {
          headers: { 'Content-Type': 'application/json' },
        }
      )
      .then((response) => {
        console.log('Response data:', response.data);
        const token = response.data.token;
        const user = response.data.user;
        const redirectPath = response.data.redirectPath; // Extrage ruta

        if(token)
        {
          localStorage.setItem('jwtToken',token);
          axios
          .get(`${process.env.REACT_APP_BACKEND1_URL}/User/FindByEmail`, {
            params: { email: email },
            headers: {
              Authorization: `Bearer ${token}` // Folosește token-ul corect
            },
          })
          .then((response) => {
            const user = response.data;
            console.log('User data:', user);

            navigate(redirectPath, { state: { user} });
          
          })
          .catch((error) => {
            console.error('Eroare la obținerea detaliilor utilizatorului:', error);
          });
      } else {
        alert('Email sau parola gresita');
      }
    })

      .catch((error) => {
        console.error('Eroare la autentificare:', error);
        alert('Email sau parola gresita');
      });
    };
    
  const onEmailChanged  =(event)=>{
   setEmail(event.target.value);

  }
  const onPasswordChanged  =(event)=>{
    setPassword(event.target.value);
 
   }
  return (
    <ThemeProvider theme={defaultTheme}>
       <Box
      sx={{
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: 'rgb(200, 173, 61)' // o culoare deschisă, albastru pal
      }}
      >
      <Container component="main" maxWidth="xs">
        <CssBaseline/>
        <Box
            sx={{
              marginTop: 18,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              backgroundColor: 'white', // Set background color to white
              borderRadius: '8px', // Optional: add border-radius for a rounded look
              padding: '20px', // Optional: add padding for spacing
            }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Logare
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Adresa de email"
              name="email"
              autoComplete="email"
              autoFocus
              onChange={onEmailChanged}
/>
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Parola"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={onPasswordChanged}

            />
           
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
             // onClick={}
            >
              Logare
            </Button>
            <Grid container>
                <Grid item>
                  <Link href="/Register" variant="body2">
                    Nu ai inca un cont? Creeaza unul!
                  </Link>
                </Grid>
              </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 8, mb: 4 }} />
      </Container>
      </Box>
    </ThemeProvider>
  );
}
