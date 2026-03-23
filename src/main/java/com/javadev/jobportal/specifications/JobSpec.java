package com.javadev.jobportal.specifications;

import com.javadev.jobportal.entity.Job;
import com.javadev.jobportal.entity.Job_;
import com.javadev.jobportal.enums.JobType;
import com.javadev.jobportal.enums.SalaryPeriod;
import jakarta.persistence.criteria.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author kyaw-san-oo
 */
public class JobSpec {

    /**
     *
     * @param search
     * @param title
     * @param minSalary
     * @param maxSalary
     * @param location
     * @param types
     * @param isNegotiable
     * @param salaryPeriods
     * @return
     */
    public static Specification<Job> getAllJobs(
            String search,
            String title,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String location,
            List<JobType> types,
            boolean isNegotiable,
            List<SalaryPeriod> salaryPeriods
            ) {
        return new Specification<Job>() {
            /**
             *
             */
            private static final Long serialVersionUID = 1L;

            @Override
            public @Nullable Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if(StringUtils.hasText(search)){
                    Predicate titlePredicate = cb.like(cb.lower(root.get(Job_.title)),"%" + search.toLowerCase() + "%");
                    Predicate locationPredicate = cb.like(cb.lower(root.get(Job_.location)),"%"+search.toLowerCase() + "%");
                    Expression<String> arrayString = cb.function("array_to_string",String.class,root.get(Job_.jobTypes),cb.literal(","));
                    Predicate jobTypePredicate = cb.like(cb.lower(arrayString),"%" + search.toLowerCase() + "%");
                    predicates.add(cb.or(titlePredicate, locationPredicate,jobTypePredicate));
                }
                if(StringUtils.hasText(title)){
                    Predicate titlePredicate = cb.like(cb.lower(root.get(Job_.title)),"%" + title.toLowerCase() + "%");
                    predicates.add(titlePredicate);
                }

                if(StringUtils.hasText(location)){
                    Predicate locationPredicate = cb.like(cb.lower(root.get(Job_.location)),"%"+location.toLowerCase()+"%");
                    predicates.add(locationPredicate);
                }
                if(types != null && !types.isEmpty()){
                    List<Predicate> typePredicate = new ArrayList<>();
                    for(var type: types){
                      Expression<String> arrayString = cb.function("array_to_string",String.class,root.get(Job_.jobTypes),cb.literal(","));
                      typePredicate.add(
                              cb.like(arrayString,"%"+type.name() + "%")
                      );
                    }
                    predicates.add(cb.or(typePredicate.toArray(new Predicate[0])));

                }
                if(minSalary != null){
                    predicates.add(cb.ge(root.get(Job_.minSalary), minSalary));
                }
                if(maxSalary != null){
                    predicates.add(cb.le(root.get(Job_.maxSalary), maxSalary));
                }
                if(isNegotiable){
                    predicates.add(cb.equal(root.get(Job_.isNegotiable), true));
                }
                if(salaryPeriods != null && !salaryPeriods.isEmpty()){
                    predicates.add(root.get(Job_.salaryPeriod).in(salaryPeriods));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
