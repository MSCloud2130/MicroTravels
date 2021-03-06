package com.microsystem.SearchService.Controllers;

import java.util.List;

import com.microsystem.SearchService.Model.TourismService;
import com.microsystem.SearchService.Repository.TourismServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import services.xsd.GetSearchRequest;
import services.xsd.GetSearchResponse;
import services.xsd.ObjectFactory;
import services.xsd.TourismServiceSOAP;

@Endpoint
public class SearchServiceController {
    
    private static final String NAMESPACE_URI = "http://services/xsd";
    private ObjectFactory factory;

    @Autowired
    private TourismServiceRepository repository;

    @Autowired
	public SearchServiceController() {
		this.factory = new ObjectFactory();
	}
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSearchRequest")
    @ResponsePayload
    public GetSearchResponse getSearch(@RequestPayload GetSearchRequest request){
        List<TourismService> entities;
        
        if(request.getTitle() == null){
            //By default if no search param is found the service retrieves all Tourism Services in DB
            entities  = repository.findAll();
        }else{
            entities = repository.findByTitleContains(request.getTitle());
        }
        GetSearchResponse response = factory.createGetSearchResponse();
        
        for (TourismService serviceEntity : entities) {
            TourismServiceSOAP service = factory.createTourismServiceSOAP();
            service.setTitle(serviceEntity.getTitle());
            service.setDescription(serviceEntity.getDescription());
            service.setPrice(serviceEntity.getPrice());
            service.setId(serviceEntity.getId());
            service.setServiceType(serviceEntity.getServiceType());
            service.setProviderId(serviceEntity.getProviderId());
            response.getService().add(service);
        }
        return response;
    }
}
