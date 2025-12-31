package com.Wissam.EasyApplier.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Wissam.EasyApplier.Model.Linkedin;

@Repository
public interface LinkedinRepository extends JpaRepository<Linkedin, Long> {

}
