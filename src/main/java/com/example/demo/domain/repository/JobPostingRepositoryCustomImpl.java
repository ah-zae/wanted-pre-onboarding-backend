package com.example.demo.domain.repository;

import com.example.demo.domain.JobPosting;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.domain.QJobPosting.jobPosting;
import static com.example.demo.domain.QCompany.company;

@Repository
@RequiredArgsConstructor
public class JobPostingRepositoryCustomImpl implements JobPostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<JobPosting> findJobPostings(Pageable pageable, String search) {
        List<JobPosting> content = queryFactory
                .selectFrom(jobPosting)
                .leftJoin(jobPosting.company, company).fetchJoin()
                .where(searchInAll(search))
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(jobPosting.count())
                .from(jobPosting)
                .where(searchInAll(search))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression searchInAll(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        return jobPosting.company.name.containsIgnoreCase(search)
                .or(jobPosting.company.country.containsIgnoreCase(search))
                .or(jobPosting.company.region.containsIgnoreCase(search))
                .or(jobPosting.position.containsIgnoreCase(search))
                .or(jobPosting.techStack.containsIgnoreCase(search));
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort) {
        return sort.stream().map(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "reward":
                    return new OrderSpecifier<>(direction, jobPosting.reward);
                case "companyName":
                    return new OrderSpecifier<>(direction, jobPosting.company.name);
                default:
                    return new OrderSpecifier<>(direction, jobPosting.id);
            }
        }).toArray(OrderSpecifier[]::new);
    }
}