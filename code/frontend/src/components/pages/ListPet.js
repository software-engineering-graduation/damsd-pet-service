import React, { useEffect, useState } from "react";
import apiService from "../../services/apiService";
import PetList from "../organisms/PetList";
import { DashboardLayout } from "@toolpad/core";
import { Box, Button, Fab, Typography } from "@mui/material";
import { Add } from "@mui/icons-material";
import LoadingSpinner from "../atoms/LoadingSpinner";

const ListPet = ({ user }) => {
  const [loading, setLoading] = useState(false);
  const [pets, setPets] = useState([]);
  const [error, setError] = useState(null);
  const [index, setIndex] = useState(0);
  const [page, setPage] = useState({
    size: 5,
    number: 0,
    totalElements: null,
    totalPages: 0,
  });

 useEffect(() => {
    const fetchPets = async () => {
      setLoading(true);

      try {
        const response = await apiService.get(
          `pet/pet-guardian/${user.id}/${index}/5`,
        );
        setPage(response.page);
        setPets((prev) => [...prev, ...response.content]);
      } catch (error) {
        setError("Erro ao carregar pets");
      } finally {
        setLoading(false);
      }
    };

    fetchPets().then((r) => console.log("Pets listados:", r));
  }, [index, user.id]);

  if (loading) {
    return (
      <DashboardLayout>
        <LoadingSpinner />
      </DashboardLayout>
    );
  }

  if (error) {
    return (
      <DashboardLayout>
        <Typography color="error">{error}</Typography>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <Box sx={{ margin: "20px" }}>
        <div
          className="signup-container"
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "left",
          }}
        >
          <h1 className="signup-title">Pets</h1>
          {page.totalElements === 0 && <div> Não há pets para esse tutor.</div>}
          {loading ? <div>Carregando pets...</div> : <PetList pets={pets} />}
        </div>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            marginTop: "20px",
          }}
        >
          {page.number < page.totalPages - 1 && (
            <Button
              variant="contained"
              onClick={() => {
                setIndex((prevIndex) => prevIndex + 1);
              }}
            >
              Carregar mais
            </Button>
          )}
        </Box>
      </Box>
      <Fab
        color="primary"
        aria-label="Add"
        style={{ position: "fixed", bottom: 20, right: 20 }}
        href={`/pets/criar`}
      >
        <Add />
      </Fab>
    </DashboardLayout>
  );
};

export default ListPet;
