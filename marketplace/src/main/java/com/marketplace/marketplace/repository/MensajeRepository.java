package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Mensaje;
import com.marketplace.marketplace.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

        // Mensajes entre dos usuarios, ordenados por fecha de envío (muy importante
        // para el chat)
        List<Mensaje> findByEmisorIdAndReceptorIdOrReceptorIdAndEmisorIdOrderByFechaEnvioAsc(
                        Long emisorId1, Long receptorId1, Long emisorId2, Long receptorId2);

        @Query("SELECT m FROM Mensaje m " +
                        "WHERE (m.emisor.id = :id1 AND m.receptor.id = :id2) " +
                        "   OR (m.emisor.id = :id2 AND m.receptor.id = :id1) " +
                        "ORDER BY m.fechaEnvio ASC")
        List<Mensaje> obtenerConversacion(@Param("id1") Long id1, @Param("id2") Long id2);

        // Todos los mensajes enviados por un usuario
        List<Mensaje> findByEmisorId(Long id);

        // Todos los mensajes recibidos por un usuario
        List<Mensaje> findByReceptorId(Long id);
}
