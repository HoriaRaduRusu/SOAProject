import {Box, Button, CssBaseline, FormControl, FormLabel, Stack, styled, TextField, Typography} from "@mui/material";
import MuiCard from "@mui/material/Card"
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import axios from "axios";
import dayjs from "dayjs";
import {createCookie} from "../Utility/cookies";
import {useNavigate} from "react-router-dom";
import {useSubscription} from "react-stomp-hooks";

const Card = styled(MuiCard)(({theme}) => ({
    display: 'flex',
    flexDirection: 'column',
    alignSelf: 'center',
    width: '100%',
    padding: theme.spacing(4),
    gap: theme.spacing(2),
}));

const SignInContainer = styled(Stack)(({theme}) => ({
    height: 'calc((1 - var(--template-frame-height, 0)) * 100dvh)',
    minHeight: '100%',
    padding: theme.spacing(2)
}));

function Login() {
    const navigate = useNavigate();
    const handleRegister = (event) => {
        const data = new FormData(event.currentTarget);
        axios.post(`${process.env.REACT_APP_API_BASE}/authentication/register`,
            {
                email: data.get('email'),
                username: data.get('username'),
                password: data.get('password'),
                birthDate: dayjs(data.get('birthDate')).format("YYYY-MM-DD")

            }).then(r => {
            createCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME, r.data.jwt)
            window.location;
        }).catch(reason => {
            alert("Invalid account; Please try again");
            console.log(reason);
        });
        event.preventDefault();
    }

    const handleLogin = (event) => {
        const data = new FormData(event.currentTarget);
        axios.post(`${process.env.REACT_APP_API_BASE}/authentication/authenticate`,
            {
                email: data.get('email'),
                password: data.get('password')
            }).then(r => {
            createCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME, r.data.jwt);
            navigate("/users");
        }).catch(reason => {
            alert("Invalid account; Please try again");
            console.log(reason);
        });
        event.preventDefault();
    }

    return (
        <Stack direction="row" justifyContent="space-around" alignContent="center">
            <CssBaseline/>
            <SignInContainer direction="column" justifyContent="space-between">
                <Card>
                    <Typography
                        component="h1"
                        variant="h4"
                        sx={{width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)'}}
                    >
                        Sign in
                    </Typography>
                    <Box
                        component="form"
                        onSubmit={handleLogin}
                        noValidate
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            width: '100%',
                            gap: 2,
                        }}
                    >
                        <FormControl>
                            <FormLabel htmlFor="email">Email</FormLabel>
                            <TextField
                                id="email"
                                type="email"
                                name="email"
                                placeholder="your@email.com"
                                autoComplete="email"
                                autoFocus
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </FormControl>
                        <FormControl>
                            <FormLabel htmlFor="password">Password</FormLabel>
                            <TextField
                                name="password"
                                placeholder="••••••"
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                autoFocus
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </FormControl>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                        >
                            Sign in
                        </Button>
                    </Box>
                </Card>
            </SignInContainer>
            <SignInContainer direction="column" justifyContent="space-between">
                <Card>
                    <Typography
                        component="h1"
                        variant="h4"
                        sx={{width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)'}}
                    >
                        Register
                    </Typography>
                    <Box
                        component="form"
                        onSubmit={handleRegister}
                        noValidate
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            width: '100%',
                            gap: 2,
                        }}
                    >
                        <FormControl>
                            <FormLabel htmlFor="email">Email</FormLabel>
                            <TextField
                                id="email"
                                type="email"
                                name="email"
                                placeholder="your@email.com"
                                autoComplete="email"
                                autoFocus
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </FormControl>
                        <FormControl>
                            <FormLabel htmlFor="text">Username</FormLabel>
                            <TextField
                                id="username"
                                type="text"
                                name="username"
                                placeholder="username"
                                autoComplete="username"
                                autoFocus
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </FormControl>
                        <FormControl>
                            <FormLabel htmlFor="password">Password</FormLabel>
                            <TextField
                                name="password"
                                placeholder="••••••"
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                autoFocus
                                required
                                fullWidth
                                variant="outlined"
                            />
                        </FormControl>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DatePicker
                                name="birthDate"
                                id="birthDate"
                                label='Birthday'
                            />
                        </LocalizationProvider>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                        >
                            Sign in
                        </Button>
                    </Box>
                </Card>
            </SignInContainer>
        </Stack>
    )
}

export default Login;