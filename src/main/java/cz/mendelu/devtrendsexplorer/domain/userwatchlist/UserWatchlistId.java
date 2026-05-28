package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWatchlistId implements Serializable {

    private String userId;
    private Long repository;
}
