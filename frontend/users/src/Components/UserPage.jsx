import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Divider,
    FormControl,
    FormLabel,
    Stack, TextField,
    Typography
} from "@mui/material";
import {
    createPost, deletePost,
    followUser,
    getCurrentUser,
    getUser,
    getUserFollowers,
    getUserFollowing,
    getUserPosts, unfollowUser
} from "../Utility/call-utility";
import {useSubscription} from "react-stomp-hooks";

function UserPage() {
    const [user, setUser] = React.useState(null);
    const [posts, setPosts] = React.useState([]);
    const [followers, setFollowers] = React.useState([]);
    const [following, setFollowing] = React.useState([]);
    const [currentUser, setCurrentUser] = React.useState(null);
    const {username} = useParams();
    useSubscription('/topic/post-' + username, (message) => {
        if (currentUser !== null && currentUser.username !== username) {
            alert(message.body + '\nRefresh if you want to see it now!');
        }
    })

    useEffect(() => {
        if (user === null) {
            getUser(username).then(r => setUser(r.data))
                .catch(response => alert("Please log in first!"));
        }
        if (!posts.length) {
            getUserPosts(username, true).then(r => setPosts(r.data))
                .catch(response => alert("Please log in first!"));
        }
        if (!followers.length) {
            getUserFollowers(username).then(r => setFollowers(r.data))
                .catch(response => alert("Please log in first!"));
        }
        if (!following.length) {
            getUserFollowing(username).then(r => setFollowing(r.data))
                .catch(response => alert("Please log in first!"));
        }
        if (currentUser === null) {
            getCurrentUser().then(r => setCurrentUser(r.data))
                .catch(response => alert("Please log in first!"));
        }
    }, [username]);

    const handleFollowUnfollow = () => {
        if (followers.findIndex(follower => currentUser !== null && currentUser.username === follower.username) !== -1) {
            unfollowUser(username).then(r => window.location.reload());
        } else {
            followUser(username).then(r => window.location.reload());
        }
    }

    const handlePost = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        createPost(data.get('title'), data.get('body')).then(r => window.location.reload())
            .catch(response => alert("Please log in first!"));
    }

    const handleDeletePost = (id) => {
        deletePost(id).then(r => window.location.reload())
            .catch(response => alert("Please log in first!"));
    }

    return (
        <Stack direction="row" sx={{display: "flex", justifyContent: "space-evenly", marginTop: "10px"}}>
            <Stack direction="column" sx={{width: "60%"}}>
                {posts.length === 0 &&
                    <Typography variant="h4"> This user hasn't posted yet! </Typography>
                }
                {posts.map((post) => (
                    <Card sx={{
                        width: "100%",
                        marginTop: "20px",
                        padding: "20px",
                        gap: "20px",
                    }}>
                        <CardContent>
                            <Typography variant="h4">{post.title}</Typography>
                            <Typography variant="h6">By {post.author}</Typography>
                            <Box sx={{
                                '& h1': {fontSize: '3rem'},
                                '& h2': {fontSize: '2.5rem'},
                                '& h3': {fontSize: '2rem'},
                                '& h4': {fontSize: '1.8rem'},
                                '& h5': {fontSize: '1.5rem'},
                                '& h6': {fontSize: '1.2rem'}
                            }} component="div">
                                <article dangerouslySetInnerHTML={{__html: post.content}}></article>
                            </Box>
                        </CardContent>
                        <CardActions>
                            {currentUser !== null && currentUser.username === username &&
                                <Button onClick={() => handleDeletePost(post.id)}>Delete Post</Button>
                            }
                        </CardActions>
                    </Card>
                ))}
            </Stack>
            <Stack direction="column" sx={{width: "30%"}}>
                <Card sx={{width: "100%"}}>
                    <CardContent>
                        <Typography>{user?.username}</Typography>
                        <Typography>{user?.email}</Typography>
                        <Typography>{new Date(user?.birthDate).toLocaleDateString()}</Typography>
                        <Divider/>
                        <Typography>Followers: {followers.length}</Typography>
                        <Typography>Following: {following.length}</Typography>
                    </CardContent>
                    <CardActions>
                        {currentUser !== null && currentUser.username !== username &&
                            <Button fullWidth
                                    onClick={handleFollowUnfollow}>{followers.findIndex(follower => currentUser !== null && currentUser.username === follower.username) !== -1 ? "Unfollow" : "Follow"}</Button>
                        }
                    </CardActions>
                </Card>
                {currentUser !== null && currentUser.username === username &&
                    <Card sx={{
                        width: "100%",
                        marginTop: "20px",
                        padding: "20px",
                        gap: "20px",
                    }}>
                        <Typography
                            component="h1"
                            variant="h4"
                            sx={{width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)'}}
                        >
                            Create new Post
                        </Typography>
                        <Box
                            component="form"
                            onSubmit={handlePost}
                            noValidate
                            sx={{
                                display: 'flex',
                                flexDirection: 'column',
                                width: '100%',
                                gap: 2,
                            }}
                        >
                            <FormControl>
                                <FormLabel htmlFor="title">Post Title</FormLabel>
                                <TextField
                                    id="title"
                                    type="text"
                                    name="title"
                                    placeholder="Title"
                                    required
                                    fullWidth
                                    variant="outlined"
                                />
                            </FormControl>
                            <FormControl>
                                <FormLabel htmlFor="body">Text Body</FormLabel>
                                <TextField
                                    name="body"
                                    placeholder="Post Content"
                                    type="text"
                                    multiline
                                    id="body"
                                    required
                                    fullWidth
                                    minRows={5}
                                    variant="outlined"
                                />
                            </FormControl>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                            >
                                Create post
                            </Button>
                        </Box>
                    </Card>}
            </Stack>
        </Stack>
    )
}

export default UserPage;