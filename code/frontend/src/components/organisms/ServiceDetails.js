import React, { useState } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  Typography,
} from "@mui/material";
import {
  Email as EmailIcon,
  Favorite as FavoriteIcon,
  Star as StarIcon,
  StarBorder as StarBorderIcon,
  StarHalf as StarHalfIcon,
} from "@mui/icons-material";
import ReviewModal from "./ReviewModal";
import { useNavigate } from "react-router-dom";

const ServiceDetails = ({ service, onClose }) => {
  const [openReviewModal, setOpenReviewModal] = useState(false);
  const navigate = useNavigate();

  if (!service) return null;

  const {
    street,
    street_number,
    additional_information,
    neighborhood,
    city,
    state,
  } = service.pet_establishment.address;

  const handleOpenReviewModal = () => {
    setOpenReviewModal(true);
  };

  const handleCloseReviewModal = () => {
    setOpenReviewModal(false);
  };

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      if (i < Math.floor(rating)) {
        stars.push(<StarIcon key={i} />);
      } else if (i < rating) {
        stars.push(<StarHalfIcon key={i} />);
      } else {
        stars.push(<StarBorderIcon key={i} />);
      }
    }
    return stars;
  };

  return (
    <>
      <Dialog open={!!service} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>
          {service.name} • {service.pet_establishment.business_name}
          <Box
            sx={{ position: "absolute", top: 8, right: 16, display: "flex" }}
          >
            {renderStars(service.pet_establishment.average_rating || 0)}
          </Box>
        </DialogTitle>
        <DialogContent>
          <Typography>{service.description}</Typography>
          <Typography>
            {service.category_name || "Não definido"} •{" "}
            {service.value ? `R$ ${service.value}` : "A definir"}
          </Typography>
          <Typography>
            {`${street}${street_number ? `, ${street_number}` : ""}${additional_information ? `, ${additional_information}` : ""}${neighborhood ? `. ${neighborhood}` : ""}${city ? `. ${city}` : ""}${state ? ` - ${state}` : ""}`}
          </Typography>
        </DialogContent>
        <Divider sx={{ margin: "16px 0" }} />
        <DialogActions
          sx={{ display: "flex", justifyContent: "space-between" }}
        >
          <Button
            startIcon={<EmailIcon />}
            onClick={() => navigate("/chat")}
            variant="contained"
            sx={{ borderRadius: "20px", flexGrow: 1 }}
          >
            Enviar Mensagem
          </Button>
          <Button
            startIcon={<FavoriteIcon />}
            onClick={handleOpenReviewModal}
            variant="contained"
            sx={{ borderRadius: "20px", flexGrow: 1 }}
          >
            Avaliar
          </Button>
        </DialogActions>
      </Dialog>

      <ReviewModal
        service={service}
        open={openReviewModal}
        onClose={handleCloseReviewModal}
      />
    </>
  );
};

export default ServiceDetails;
