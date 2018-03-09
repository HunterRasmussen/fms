package fms.facade;


import java.util.List;

import fms.models.EventModel;
import fms.models.PersonModel;
import fms.models.UserModel;

public class LoadRequest {
    List<UserModel> users;
    List<PersonModel> persons;
    List<EventModel> events;

}
