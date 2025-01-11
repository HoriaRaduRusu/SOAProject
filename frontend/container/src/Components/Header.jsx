import {AppBar, Box, Container, Toolbar, Typography} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";
import {deleteCookie} from "../Utility/cookies";

function Header() {
    const navigator = useNavigate();

    const handleLogOut = () => {
        deleteCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME);
        navigator("/");
    }

    return (
        <AppBar position="static">
            <Container maxWidth="x1">
                <Toolbar disableGutters>
                    <Typography
                        variant="h4"
                        sx={{
                            mr: 2
                        }}>
                        SocMed
                    </Typography>
                    <Box display={"flex"}>
                        <Box mr={2}>
                            <Link to="/">
                                Home
                            </Link>
                        </Box>
                        <Box mr={2}>
                            <Link to="/users">
                                Users
                            </Link>
                        </Box>
                        <Box>
                            <Typography sx={{cursor: 'pointer'}} onClick={handleLogOut}>Log Out</Typography>
                        </Box>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    )
}

export default Header;