import { Box, Button, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

function App() {
  return (
    <>
      <Box>
        <Box sx={{ marginBottom: "5rem" }}>
          <Typography variant="h3">
            Welcome to the G-purpose app ! 🤖
          </Typography>
        </Box>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </Box>
      <h1>Vite + React</h1>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
      <Box sx={{ marginTop: "3rem" }}>
        <Link to={"/dashboard"}>
          <Button variant="outlined">Dashboard</Button>
        </Link>
      </Box>
    </>
  );
}

export default App;
