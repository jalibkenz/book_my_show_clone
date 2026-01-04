package in.kenz.bookmyshow.show.service;

import in.kenz.bookmyshow.show.dto.CreateShowRequest;
import in.kenz.bookmyshow.show.entity.Show;

public interface ShowService {
    Show createShow(CreateShowRequest request);
}
