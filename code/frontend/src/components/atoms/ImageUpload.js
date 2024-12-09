import React, { useState } from "react";
import { Box, CircularProgress, IconButton, Typography } from "@mui/material";
import { Clear, CloudUpload } from "@mui/icons-material";
import { message } from "antd";

const ImageUpload = ({ label, value, onChange }) => {
  const [loading, setLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState(value || null);

  const handleBeforeUpload = (file) => {
    const isImage = file.type.startsWith("image/");
    if (!isImage) {
      message.error("Você só pode enviar arquivos de imagem");
      return false;
    }
    return true;
  };

  const handleChange = async (event) => {
    const file = event.target.files?.[0];
    if (file) {
      if (!handleBeforeUpload(file)) return;

      setLoading(true);
      const formData = new FormData();
      formData.append("image", file);

      try {
        const response = await fetch(
          "https://api.imgbb.com/1/upload?expiration=10&key=64170b4c16787170f413fdd85b9288fa",
          {
            method: "POST",
            body: formData,
          },
        );

        const data = await response.json();
        if (data.success) {
          setImageUrl(data.data.url);
          onChange(data.data.url);
        } else {
          message.error("Falha ao fazer upload da imagem");
        }
      } catch (error) {
        message.error(
          "Ocorreu um erro ao fazer upload da imagem: " + error.message,
        );
      } finally {
        setLoading(false);
      }
    }
  };

  const handleClear = () => {
    setImageUrl("");
    onChange("");
  };

  return (
    <Box
      sx={{
        textAlign: "center",
        border: "2px dashed #ccc",
        padding: 2,
        borderRadius: 1,
      }}
    >
      <Typography variant="subtitle1" gutterBottom>
        {label}
      </Typography>
      <input
        accept="image/*"
        style={{ display: "none" }}
        id="upload-button-file"
        type="file"
        onChange={handleChange}
      />
      <label htmlFor="upload-button-file">
        <IconButton component="span">
          {loading ? <CircularProgress size={24} /> : <CloudUpload />}
        </IconButton>
      </label>
      {imageUrl && (
        <Box sx={{ mt: 2, position: "relative" }}>
          <img
            src={imageUrl}
            alt="preview"
            style={{ width: "100%", maxHeight: 200, objectFit: "cover" }}
          />
          <IconButton
            onClick={handleClear}
            sx={{ position: "absolute", top: 8, right: 8, background: "#fff" }}
          >
            <Clear />
          </IconButton>
        </Box>
      )}
    </Box>
  );
};

export default ImageUpload;
