package br.com.pfsafe.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pfsafe.model.File;

public interface FileRepository extends JpaRepository<File, UUID> {
  Optional<File> findByUniqueName(String name);

  boolean existsByUniqueName(String uniqueName);
}
