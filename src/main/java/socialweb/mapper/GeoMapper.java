package socialweb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import socialweb.dto.CityDto;
import socialweb.dto.CountryDto;
import socialweb.entity.Geo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeoMapper {
    @Mapping(target = "title", source = "name")
    CountryDto toCountryDto(Geo geo);
    List<CountryDto> toCountryDtoList(List<Geo> geos);
    @Mapping(target = "title", source = "name")
    @Mapping(target = "countryId", source = "parentId")
    CityDto toCityDto(Geo geo);
    List<CityDto> toCityDtoList(List<Geo> geos);
}
