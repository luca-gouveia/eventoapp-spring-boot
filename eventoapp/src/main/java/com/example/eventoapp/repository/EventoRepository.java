package com.example.eventoapp.repository;

import com.example.eventoapp.models.Evento;
import org.springframework.data.repository.CrudRepository;

public interface EventoRepository extends CrudRepository<Evento, String>{
    Evento findById(long id);
}
