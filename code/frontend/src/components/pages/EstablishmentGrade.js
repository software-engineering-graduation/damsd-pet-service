import React, { useState, useEffect } from 'react';
import { Box, Typography } from '@mui/material';
import DashboardLayout from '../templates/DashboardLayout';
import Button from '../atoms/Button';
import LoadingSpinner from '../atoms/LoadingSpinner';
import ErrorMessage from '../atoms/ErrorMessage';
import EstablishmentCard from '../molecules/EstablishmentCard';
import authService from '../../services/authService';
import './EstablishmentGrade.scss';


const EstablishmentGrade = ({ user, onSubmit }) => {
    const [rating, setRating] = useState(0); // Valor da avaliação selecionada
    const [hoverRating, setHoverRating] = useState(0); // Valor da avaliação com o hover
    const [comment, setComment] = useState(''); // Comentário do usuário

    const handleRating = (value) => {
        setRating(value); // Define o rating com o valor clicado
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (rating === 0) {
            alert('Por favor, selecione uma nota.');
            return;
        }

        const review = {
            rating,
            comment,
        };

        if (onSubmit) {
            onSubmit(review);
        }

        setRating(0);
        setComment('');
    };

    return (
        <DashboardLayout>
            <div className="establishment-grade-container">
                <h2>Avalie o Estabelecimento</h2>

                <div className="rating-stars">
                    {[1, 2, 3, 4, 5].map((star) => (
                        <span
                            key={star}
                            className={`star ${star <= (hoverRating || rating) ? 'filled' : ''}`}
                            onClick={() => handleRating(star)}
                            onMouseEnter={() => setHoverRating(star)}
                            onMouseLeave={() => setHoverRating(0)}
                        >
                            ★
                        </span>
                    ))}
                </div>

                <textarea
                    placeholder="Escreva seu comentário..."
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                ></textarea>

                <button onClick={handleSubmit} className="send-btn">Enviar Avaliação</button>
            </div>
        </DashboardLayout>
    );
};

export default EstablishmentGrade;
