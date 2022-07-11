package com.redhat.custom.sap.data;

import java.util.List;
import java.util.stream.Collectors;

public class SapDataRepresentation {

    public class Wan {
        private String wanName;
        private List<Homesite> homesites;

        public void setWanName(String wanName) {
            this.wanName = wanName;
        }

        public String getWanName() {
            return wanName;
        }

        public void setHomesites(List<Homesite> homesites) {
            this.homesites = homesites;
        }

        public List<Homesite> getHomesites() {
            return homesites;
        }

        public void addHomesite(Homesite homesite) {
            homesites.add(homesite);
        }

        public void addHomesite(String homesiteName) {
            Homesite homesite = new Homesite();
            homesite.setHomesiteName(homesiteName);
            homesites.add(homesite);

        }

        public Homesite getHomesite(String homesiteName) {
            return homesites.stream().filter(homesite -> homesite.getHomesiteName().equals(homesiteName)).findFirst().orElse(null);
        }

    }

    public class Homesite {
        private String homesiteName;
        private List<Service> services;

        public void setHomesiteName(String homesiteName) {
            this.homesiteName = homesiteName;
        }

        public String getHomesiteName() {
            return homesiteName;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

        public List<Service> getServices() {
            return services;
        }

        public void addService(Service service) {
            services.add(service);
        }

        public void addService(String serviceId, String serviceUrl) {
            Service service = new Service();
            service.setServiceId(serviceId);
            service.setServiceUrl(serviceUrl);
            services.add(service);
        }

        public Service getService(String serviceId) {
            return services.stream().filter(service -> service.getServiceId().equals(serviceId)).findFirst().orElse(null);
        }
    }

    public static class Service {
        private String serviceId;
        private String serviceUrl;

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

    }

    private List<Wan> wans;

    public void setWans(List<Wan> wans) {
        this.wans = wans;
    }

    public List<Wan> getWans() {
        return wans;
    }

    public Wan getWan(String wanName) {
        return wans.stream().filter(wan -> wan.getWanName().equals(wanName)).findFirst().orElse(null);
    }

    public void addWan(Wan wan) {
        wans.add(wan);
    }

    public void addWan(String wanName) {
        Wan wan = new Wan();
        wan.setWanName(wanName);
        wans.add(wan);
    }

    public void setHomesites(Wan wan, List<Homesite> homesites) {
        wan.setHomesites(homesites);
    }

    public Homesite getHomesite(String wanName, String homesiteName) {
        Wan wan = getWan(wanName);
        if (wan == null) {
            return null;
        }
        return wan.getHomesite(homesiteName);
    }

    public List<Homesite> getHomesites(String wanName) {
        Wan wan = getWan(wanName);
        if (wan == null) {
            return null;
        }
        return wan.getHomesites();
    }

    public void addHomesite(Wan wan, Homesite homesite) {
        wan.addHomesite(homesite);
    }

    public void addHomesite(String wanName, Homesite homesite) {
        Wan wan = getWan(wanName);
        if (wan == null) {
            wan = new Wan();
            wan.setWanName(wanName);
            wans.add(wan);
        }
        wan.addHomesite(homesite);
    }

    public void setService(Wan wan, Homesite homesite, Service service) {
        homesite.addService(service);
    }

    public void setService(Wan wan, String homesiteName, Service service) {
        Homesite homesite = getHomesite(wan.getWanName(), homesiteName);
        if (homesite == null) {
            return;
        }
        homesite.addService(service);
    }

    public void setService(String wanName, String homesiteName, Service service) {
        Wan wan = getWan(wanName);
        if (wan == null) {
            return;
        }
        Homesite homesite = getHomesite(wan.getWanName(), homesiteName);
        if (homesite == null) {
            return;
        }
        homesite.addService(service);
    }

    public void setServices(Homesite homesite, List<Service> services) {
        homesite.setServices(services);
    }

    public Service getService(Homesite homesite, String serviceId) {
        Service service = homesite.getService(serviceId);
        return service;
    }

    public Service getService(String wanName, String homesiteName, String serviceId) {
        Homesite homesite = getHomesite(wanName, homesiteName);
        if (homesite == null) {
            return null;
        }
        return homesite.getService(serviceId);
    }

    public List<Service> getServices(Homesite homesite) {
        return homesite.getServices();
    }

    public void addServices(Homesite homesite, List<Service> services) {
        homesite.setServices(services);
    }

    public void addService(String wanName, String homesiteName, Service service) {
        Homesite homesite = getHomesite(wanName, homesiteName);
        if (homesite == null) {
            homesite = new Homesite();
            homesite.setHomesiteName(homesiteName);
            addHomesite(wanName, homesite);
        }
        homesite.addService(service);
    }

    public void addService(String wanName, String homesiteName, String serviceId, String serviceUrl) {
        Service service = new Service();
        service.setServiceId(serviceId);
        service.setServiceUrl(serviceUrl);
        addService(wanName, homesiteName, service);
    }

    public String getServiceUrl(Service service) {
        return service.getServiceUrl();
    }

    public List<String> getServiceUrls(Homesite homesite) {
        return homesite.getServices().stream().map(Service::getServiceUrl).collect(Collectors.toList());
    }

    public List<String> getAllServiceUrls(Wan wan) {
        return wan.getHomesites().stream().flatMap(homesite -> getServiceUrls(homesite).stream()).collect(Collectors.toList());
    }

    public List<String> getAllServiceUrls() {
        return wans.stream().flatMap(wan -> getAllServiceUrls(wan).stream()).collect(Collectors.toList());
    }

    public List<String> getAllHomesiteNames(Wan wan) {
        return wan.getHomesites().stream().map(Homesite::getHomesiteName).collect(Collectors.toList());
    }

}
