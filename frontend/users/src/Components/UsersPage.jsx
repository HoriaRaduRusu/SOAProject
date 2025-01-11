import {useEffect, useState} from "react";
import axios from "axios";
import {getCookie} from "../Utility/cookies";
import {Grid} from "@mui/material";
import UserCard from "./UserCard";
import {getUsers} from "../Utility/call-utility";

export default ({loading, setLoading}) => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        if (!users.length) {
            getUsers().then(r => setUsers(r.data))
                .catch(response => alert("Please log in first!"));
        }
    }, [users]);

    return (
        <Grid container sx={{marginTop: "10px"}}>
            {users.map((user) =>
                <UserCard user={user} />
            )}
        </Grid>
    )
}