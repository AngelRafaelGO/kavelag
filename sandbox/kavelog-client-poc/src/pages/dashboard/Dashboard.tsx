import { Box, Button, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";

const Dashboard = () => {
  const [socket, setSocket] = useState<WebSocket | null>(null);
  const [serverMessage, setServerMessage] = useState<any>("");

  const handleTestServer = () => {
    if (socket) {
      socket.send(serverMessage);
      console.log("Sent message to server");
    } else {
      console.log("WebSocket is not connected");
    }
  };

  const handleServerMessage = (message: String) => {
    setServerMessage(message);
  };

  useEffect(() => {
    const ws = new WebSocket("ws://127.0.0.1:8080/ws");

    // Connection opened
    ws.addEventListener("open", (event) => {
      console.log("Connected to WebSocket server");
      ws.send("Hello from client!");
    });

    // Listen for messages
    ws.addEventListener("message", (event) => {
      console.log("Message from server: ", event.data);
    });

    // Handle socket closure
    ws.addEventListener("close", () => {
      console.log("WebSocket connection closed");
    });

    // Handle errors
    ws.addEventListener("error", (error) => {
      console.error("WebSocket error: ", error);
    });

    setSocket(ws);

    // Clean up WebSocket on component unmount
    return () => {
      ws.close();
    };
  }, []);

  return (
    <Box>
      <Box>
        <Typography variant="h2">Dasboard</Typography>
        <Typography sx={{ marginTop: "4rem" }}>
          Data will be displayed here
        </Typography>
        <Box>
          <TextField
            onChange={(event) => handleServerMessage(event.target.value)}
          />
          <Button onClick={() => handleTestServer()}>TEST THE SERVER</Button>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
