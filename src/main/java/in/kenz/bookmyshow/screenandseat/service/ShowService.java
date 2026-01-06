package in.kenz.bookmyshow.screenandseat.service;

import in.kenz.bookmyshow.screenandseat.dto.CreateShowRequest;
import in.kenz.bookmyshow.screenandseat.entity.Show;

public interface ShowService {
    Show createShow(CreateShowRequest request);
}
