import { Box, Button, Typography } from "@mui/material";
import { useEffect, useState } from "react";

const Dashboard = () => {
  const [socket, setSocket] = useState<WebSocket | null>(null);

  const handleTestServer = () => {
    if (socket) {
      socket.send("Test message from client");
      console.log("Sent message to server");
    } else {
      console.log("WebSocket is not connected");
    }
  };

  useEffect(() => {
    const ws = new WebSocket("ws://127.0.0.1:9002/ws");

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
          <Button onClick={() => handleTestServer()}>TEST THE SERVER</Button>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard;
