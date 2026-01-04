package in.kenz.bookmyshow.theatre.service.impl;


import in.kenz.bookmyshow.common.exception.DuplicateResourceException;
import in.kenz.bookmyshow.common.exception.ResourceNotFoundException;
import in.kenz.bookmyshow.theatre.dto.SignupTheatreRequest;
import in.kenz.bookmyshow.theatre.dto.UpdateTheatreRequest;
import in.kenz.bookmyshow.theatre.entity.Theatre;
import in.kenz.bookmyshow.theatre.enums.ProfileStatus;
import in.kenz.bookmyshow.theatre.event.TheatreCreatedEvent;

import in.kenz.bookmyshow.theatre.event.TheatreProfileUpdatedEvent;
import in.kenz.bookmyshow.theatre.repository.TheatreRepository;
import in.kenz.bookmyshow.theatre.service.TheatreService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.util.StringUtils.hasText;

@Service
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TheatreServiceImpl(TheatreRepository theatreRepository, ApplicationEventPublisher eventPublisher) {
        this.theatreRepository = theatreRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public Theatre createTheatre(SignupTheatreRequest signupTheatreRequest) {
        if (theatreRepository.existsByName(signupTheatreRequest.getName())) {
            throw new DuplicateResourceException("Theatre Name is already in use");
        }


        Theatre theatre = Theatre.builder()
                .name(signupTheatreRequest.getName())
                .city(signupTheatreRequest.getCity())
                .state(signupTheatreRequest.getState())
                .country(signupTheatreRequest.getCountry())
                .email(signupTheatreRequest.getEmail())
                .mobile(signupTheatreRequest.getMobile())
                .profileStatus(in.kenz.bookmyshow.theatre.enums.ProfileStatus.ACTIVE)
                .build();
        Theatre savedTheatre = theatreRepository.save(theatre);

        // 🔔 Trigger email event
        eventPublisher.publishEvent(
                new TheatreCreatedEvent(
                        savedTheatre.getName(),

                        savedTheatre.getCity(),
                        savedTheatre.getState(),
                        savedTheatre.getCountry(),

                        savedTheatre.getEmail(),
                        savedTheatre.getMobile()
                )
        );
        return savedTheatre;
    }




    @Override
    public Theatre fetchTheatre(UUID theatreId) {
        return theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre ID is invalid"));
    }



    @Transactional
    public Theatre updateTheatre(UUID theatreId, UpdateTheatreRequest updateTheatreRequest) {

        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        // ======================
        // Capture OLD values
        // ======================
        String oldName = theatre.getName();
        String oldAddress = theatre.getAddress();

        String oldCity = theatre.getCity();
        String oldState = theatre.getState();
        String oldCountry = theatre.getCountry();

        String oldEmail = theatre.getEmail();
        String oldMobile = theatre.getMobile();

        ProfileStatus oldProfileStatus = theatre.getProfileStatus();

        String oldEmergencyContactName = theatre.getEmergencyContactName();
        String oldEmergencyContactEmail = theatre.getEmergencyContactEmail();
        String oldEmergencyContactMobile = theatre.getEmergencyContactMobile();

        // ======================
        // THEATRE NAME UNIQUENESS
        // ======================
        if (hasText(updateTheatreRequest.getName())
                && !updateTheatreRequest.getName().equals(theatre.getName())) {

            if (theatreRepository.existsByNameAndIdNot(
                    updateTheatreRequest.getName(), theatreId)) {
                throw new DuplicateResourceException("Theatre Name already exists");
            }

            theatre.setName(updateTheatreRequest.getName());
        }


        // ======================
        // SIMPLE FIELDS
        // ======================
        setIfHasText(updateTheatreRequest.getName(), theatre::setName);
        setIfHasText(updateTheatreRequest.getAddress(), theatre::setAddress);

        setIfHasText(updateTheatreRequest.getCity(), theatre::setCity);
        setIfHasText(updateTheatreRequest.getState(), theatre::setState);
        setIfHasText(updateTheatreRequest.getCountry(), theatre::setCountry);

        setIfHasText(updateTheatreRequest.getEmail(), theatre::setEmail);
        setIfHasText(updateTheatreRequest.getMobile(), theatre::setMobile);

        setIfHasText(updateTheatreRequest.getEmergencyContactName(), theatre::setEmergencyContactName);
        setIfHasText(updateTheatreRequest.getEmergencyContactEmail(), theatre::setEmergencyContactEmail);
        setIfHasText(updateTheatreRequest.getEmergencyContactMobile(), theatre::setEmergencyContactMobile);

        // ======================
        // ENUM
        // ======================
        if (updateTheatreRequest.getProfileStatus() != null) {
            theatre.setProfileStatus(updateTheatreRequest.getProfileStatus());
        }

        theatreRepository.save(theatre);

        // ======================
        // Publish event ONLY if profile-related fields changed
        // ======================
        boolean profileChanged =
                !Objects.equals(oldName, theatre.getName())
                        || !Objects.equals(oldAddress, theatre.getAddress())

                        || !Objects.equals(oldCity, theatre.getCity())
                        || !Objects.equals(oldState, theatre.getState())
                        || !Objects.equals(oldCountry, theatre.getCountry())

                        || !Objects.equals(oldEmail, theatre.getEmail())
                        || !Objects.equals(oldMobile, theatre.getMobile())

                        || !Objects.equals(oldProfileStatus, theatre.getProfileStatus())

                        || !Objects.equals(oldEmergencyContactName, theatre.getEmergencyContactName())
                        || !Objects.equals(oldEmergencyContactEmail, theatre.getEmergencyContactEmail())
                        || !Objects.equals(oldEmergencyContactMobile, theatre.getEmergencyContactMobile());

        // 🔔 Trigger email event
        if (profileChanged) {
            eventPublisher.publishEvent(
                    new TheatreProfileUpdatedEvent(

                            // Basic profile
                            oldName, theatre.getName(),
                            oldAddress, theatre.getAddress(),

                            oldCity, theatre.getCity(),
                            oldState, theatre.getState(),
                            oldCountry, theatre.getCountry(),

                            oldEmail, theatre.getEmail(),
                            oldMobile, theatre.getMobile(),

                            oldProfileStatus, theatre.getProfileStatus(),

                            // Emergency contact
                            oldEmergencyContactName, theatre.getEmergencyContactName(),
                            oldEmergencyContactEmail, theatre.getEmergencyContactEmail(),
                            oldEmergencyContactMobile, theatre.getEmergencyContactMobile()
                    )
            );
        }

        return theatre;
    }





    //helper method
    private void setIfHasText(String newValue, Consumer<String> stringConsumer) {
        if (StringUtils.hasText(newValue)) {
            stringConsumer.accept(newValue);
        }
    }






    @Override
    public Page<Theatre> fetchAllTheatres(Pageable theatresListPageable) {
        return theatreRepository.findAll(theatresListPageable);
    }





    @Override
    @Transactional
    public Theatre deactivateTheatre(UUID theatreId) {

        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatre.setProfileStatus(ProfileStatus.INACTIVE);

        return theatreRepository.save(theatre);
    }





    @Override
    @Transactional
    public Theatre activateTheatre(UUID theatreId) {

        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatre.setProfileStatus(ProfileStatus.ACTIVE);

        return theatreRepository.save(theatre);
    }




    @Override
    @Transactional
    public Theatre softdeleteTheatre(UUID theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatre.setProfileStatus(ProfileStatus.DELETED);
        return theatre;
    }




    //do call this after soft delete so in log the status of SOFT_DELETED will be there
    @Override
    @Transactional
    public void deleteTheatre(UUID theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
        theatreRepository.delete(theatre);
    }



}
