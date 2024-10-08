package ru.yandex.kardomoblieapp.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.kardomoblieapp.location.dto.Location;
import ru.yandex.kardomoblieapp.location.model.City;
import ru.yandex.kardomoblieapp.location.model.Country;
import ru.yandex.kardomoblieapp.location.model.Region;
import ru.yandex.kardomoblieapp.location.repository.CityRepository;
import ru.yandex.kardomoblieapp.location.repository.CountryRepository;
import ru.yandex.kardomoblieapp.location.repository.RegionRepository;
import ru.yandex.kardomoblieapp.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final CountryRepository countryRepository;

    private final RegionRepository regionRepository;

    private final CityRepository cityRepository;

    /**
     * Получение списка всех стран.
     *
     * @return список стран
     */
    @Override
    public List<Country> getAllCountries() {
        List<Country> countries = countryRepository.findAllCountriesOrderByName();
        log.debug("Получен список всех стран");
        return countries;
    }

    /**
     * Получение страны по идентификатору.
     *
     * @param countryId идентификатор страны
     * @return найденная страна
     */
    @Override
    public Country getCountryById(long countryId) {
        Country country = getCountry(countryId);
        log.debug("Получена страна с id '{}'.", countryId);
        return country;
    }

    /**
     * Получение списка регионов страны.
     *
     * @param countryId идентификатор страны
     * @return список регионов страны
     */
    @Override
    public List<Region> getAllCountryRegions(long countryId) {
        List<Region> regions = regionRepository.findAllByCountryIdOrderByName(countryId);
        log.debug("Получены все регионы страны с id '{}'.", countryId);
        return regions;
    }

    /**
     * Получение региона по идентификатору.
     *
     * @param regionId идентификатор региона
     * @return найденный регион
     */
    @Override
    public Region getRegionById(long regionId) {
        Region region = getRegion(regionId);
        log.debug("Получен регион с id '{}'.", regionId);
        return region;
    }

    /**
     * Добавление города.
     *
     * @param city город
     * @return сохраненный город
     */
    @Override
    public City addCity(City city) {
        City savedCity = cityRepository.save(city);
        log.debug("Добавлен город c id '{}'.", savedCity.getId());
        return savedCity;
    }

    /**
     * Поиск города по его названию, стране и региону.
     *
     * @param cityName  название города
     * @param countryId идентификатор страны
     * @param regionId  идентификатор региона
     * @return найденный город
     */
    @Override
    public Optional<City> findCityByNameCountryAndRegion(String cityName, Long countryId, Long regionId) {
        Optional<City> city = cityRepository.findByNameAndCountryIdAndRegionId(cityName, countryId, regionId);
        log.debug("Поиск наличия города с названием '{}' и регионом с id'{}'.", cityName, regionId);
        return city;
    }

    /**
     * Удаление всех добавленных городов.
     */
    @Override
    public void deleteAllCities() {
        cityRepository.deleteAll();
        log.info("Удаление всех городов.");
    }

    @Override
    public Location getLocation(Long countryId, Long regionId, String cityName) {
        final Location location = new Location();

        Country country = null;
        if (countryId != null) {
            country = getCountryById(countryId);
            location.setCountry(country);
        }
        Region region = null;
        if (regionId != null) {
            region = getRegionById(regionId);
            location.setRegion(region);
        }

        if (cityName != null) {
            Optional<City> city = findCityByNameCountryAndRegion(cityName, countryId, regionId);

            if (city.isPresent()) {
                location.setCity(city.get());
            } else {
                final City newCity = City.builder()
                        .name(cityName)
                        .country(country)
                        .region(region)
                        .build();
                City savedCity = addCity(newCity);
                location.setCity(savedCity);
            }
        }
        return location;
    }

    private Country getCountry(long countryId) {
        return countryRepository.findById(countryId).orElseThrow(
                () -> new NotFoundException("Страна с id '" + countryId + "' не найдена."));
    }

    private Region getRegion(long regionId) {
        return regionRepository.findByRegionId(regionId)
                .orElseThrow(() -> new NotFoundException("Регион с id '" + regionId + "' не найден."));
    }
}
