package com.example.carrentalsystem.Config;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Repositories.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Configuration
public class RepositoryInitializer {
    private final FuelTypeRepository fuelTypeRepository;
    private final RoleRepository roleRepository;
    private final CarImageRepository carImageRepository;
    private final RentalStatusRepository rentalStatusRepository;

    public RepositoryInitializer(FuelTypeRepository fuelTypeRepository, RoleRepository roleRepository,
                                 CarImageRepository carImageRepository, RentalStatusRepository rentalStatusRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.roleRepository = roleRepository;
        this.carImageRepository = carImageRepository;
        this.rentalStatusRepository = rentalStatusRepository;
    }

    @Bean
    InitializingBean init() {
        return () -> {
            if(fuelTypeRepository.findAll().isEmpty()){
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_DIESEL));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_GASOLINE));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_LPG));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_ELECTRIC));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_HYBRID));
            }

            if(rentalStatusRepository.findAll().isEmpty()){
                rentalStatusRepository.save(new RentalStatus(ERentalStatus.STATUS_ACCEPTED));
                rentalStatusRepository.save(new RentalStatus(ERentalStatus.STATUS_CANCELLED));
                rentalStatusRepository.save(new RentalStatus(ERentalStatus.STATUS_PENDING));
                rentalStatusRepository.save(new RentalStatus(ERentalStatus.STATUS_REJECTED));
            }

            if(roleRepository.findAll().isEmpty()){
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
                roleRepository.save(new Role(ERole.ROLE_USER));
            }

            if(carImageRepository.findAll().isEmpty()){
                carImageRepository.save(
                        new CarImage(
                                imageFromURLToByteArray(new URL("https://icon-library.com/images/no-image-icon/no-image-icon-0.jpg"))
                        )
                );
            }
        };
    }

    public static byte[] imageFromURLToByteArray(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(conn.getInputStream(), baos);

        return baos.toByteArray();
    }
}
