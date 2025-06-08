package com.example.sfmproject.Controllers;

import com.example.sfmproject.Entities.Departement;
import com.example.sfmproject.Entities.Organisation;
import com.example.sfmproject.Services.IDepartementService;
import com.example.sfmproject.Services.IOrganisationService;
import com.example.sfmproject.Services.IDepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    @Autowired
    private IOrganisationService organisationService;
    @Autowired
    private IDepartementService departementService;


    // Get all organisations
    @GetMapping
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        List<Organisation> organisations = organisationService.getAllOrganisations();
        return ResponseEntity.ok(organisations);
    }

    // Get an organisation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable Long id) {
        Organisation organisation = organisationService.getOrganisationById(id);
        return organisation != null ? ResponseEntity.ok(organisation) : ResponseEntity.notFound().build();
    }

    // Add a new organisation
    @PostMapping
    public ResponseEntity<Organisation> addOrganisation(@RequestBody Organisation organisation) {
        organisationService.saveOrganisation(organisation);
        return ResponseEntity.ok(organisation);
    }

    // Update an existing organisation
    @PutMapping("/{id}")
    public ResponseEntity<Organisation> updateOrganisation(@PathVariable Long id, @RequestBody Organisation updatedOrganisation) {
        Organisation existingOrganisation = organisationService.getOrganisationById(id);

        if (existingOrganisation != null) {
            existingOrganisation.setNomOrganisation(updatedOrganisation.getNomOrganisation());
            existingOrganisation.setAddress(updatedOrganisation.getAddress());
            organisationService.updateOrganisation(id);
            return ResponseEntity.ok(existingOrganisation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an organisation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrganisation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/hierarchy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Organisation>> getHierarchyForAdmin() {
        return ResponseEntity.ok(organisationService.getAllDataForAdmin());
    }

    @GetMapping("/{orgId}/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Departement>> getDepartmentsByOrganisation(@PathVariable Long orgId) {
        List<Departement> departements = departementService.getDepartmentsByOrganisation(orgId);
        return ResponseEntity.ok(departements);
    }


}
