// src/components/ReviewModal.js
import React, { useState } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from "@mui/material";
import { Star as StarIcon } from "@mui/icons-material";
import apiService from "../../services/apiService";

const ReviewModal = ({ service, open, onClose }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");
  const user = JSON.parse(localStorage.getItem("user"));

  const handleRatingChange = (index) => {
    setRating(index + 1);
  };

  const handleSendReview = () => {
    let reviewData = {
      author_id: user.id,
      reviewed_user_id: service.pet_establishment.id,
      rating: rating,
      comment: comment,
    };
    apiService.post("review", reviewData).then((r) => {
      onClose();
      window.location.reload();
    });
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Avaliar {service.pet_establishment.business_name}</DialogTitle>
      <DialogContent>
        <Box sx={{ display: "flex", marginBottom: "16px" }}>
          {[...Array(5)].map((_, index) => (
            <Button
              key={index}
              onClick={() => handleRatingChange(index)}
              sx={{
                color: index < rating ? "gold" : "gray",
                fontSize: "2rem",
                padding: 0,
              }}
            >
              <StarIcon />
            </Button>
          ))}
        </Box>
        <TextField
          label="Comentário"
          multiline
          rows={4}
          fullWidth
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          variant="outlined"
          sx={{ marginBottom: "16px" }}
        />
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose} color="secondary">
          Fechar
        </Button>
        <Button onClick={handleSendReview} variant="contained" color="primary">
          Enviar
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ReviewModal;
