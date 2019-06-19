package com.example.eventoapp.repository;

import com.example.eventoapp.models.Convidado;
import com.example.eventoapp.models.Evento;
import org.springframework.data.repository.CrudRepository;

public interface ConvidadoRepository extends CrudRepository<Convidado, String> {
    Iterable<Convidado> findByEvento(Evento evento);
    Convidado findByCpf(String cpf);

}
