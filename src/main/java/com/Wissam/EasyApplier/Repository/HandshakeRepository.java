package com.Wissam.EasyApplier.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.Handshake;

@Repository
public interface HandshakeRepository extends JpaRepository<Handshake, Long> {

  Optional<Handshake> findByEmail(String email);

}
