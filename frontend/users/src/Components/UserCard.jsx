import {Box, Card, CardContent, Typography} from "@mui/material";
import {Link} from "react-router-dom";

export default ({user}) => {
    return (
        <Box sx={{minWidth: 275}}>
            <Card variant="outlined">
                <CardContent>
                    <Typography
                        variant="h5"
                        component={Link}
                        to={user.username}
                        >
                        {user.username}
                    </Typography>
                    <Typography sx={{mb: 1.5}}>
                        {user.email}
                    </Typography>
                </CardContent>
            </Card>
         </Box>
    )
}