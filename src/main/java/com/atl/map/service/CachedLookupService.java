package com.atl.map.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.atl.map.entity.BuildingEntity;
import com.atl.map.entity.MarkerEntity;
import com.atl.map.entity.PostListViewEntity;
import com.atl.map.repository.BuildingRepository;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.repository.PostListViewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CachedLookupService {

    private static final long FIVE_MINUTE_BUCKET_SECONDS = 300L;

    private final BuildingRepository buildingRepository;
    private final PostListViewRepository postListViewRepository;
    private final MarkerRepository markerRepository;

    @Cacheable(cacheNames = "buildingList", key = "'all'")
    public List<BuildingEntity> getBuildingList() {
        return BuildingEntity.copyList(buildingRepository.getBuildingList());
    }

    @Cacheable(cacheNames = "topPosts", key = "#bucketKey")
    public List<PostListViewEntity> getTopPosts(long bucketKey) {
        LocalDateTime beforeWeek = getBucketDateTime(bucketKey).minusWeeks(1);
        return new ArrayList<>(
                postListViewRepository.findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(beforeWeek));
    }

    @Cacheable(cacheNames = "topMarkers", key = "#bucketKey")
    public List<MarkerEntity> getTopMarkers(long bucketKey) {
        LocalDateTime beforeWeek = getBucketDateTime(bucketKey).minusWeeks(1);
        return new ArrayList<>(markerRepository.findMarkersByLikesSinceDate(beforeWeek, PageRequest.of(0, 20)));
    }

    public long getCurrentFiveMinuteBucketKey() {
        return Instant.now().getEpochSecond() / FIVE_MINUTE_BUCKET_SECONDS;
    }

    private LocalDateTime getBucketDateTime(long bucketKey) {
        long bucketEpochSeconds = bucketKey * FIVE_MINUTE_BUCKET_SECONDS;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(bucketEpochSeconds), ZoneId.systemDefault());
    }
}
