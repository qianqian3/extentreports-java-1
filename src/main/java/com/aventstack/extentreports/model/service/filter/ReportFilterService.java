package com.aventstack.extentreports.model.service.filter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Author;
import com.aventstack.extentreports.model.Category;
import com.aventstack.extentreports.model.Device;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.model.context.NamedAttributeTestContext;
import com.aventstack.extentreports.model.context.filter.NamedAttributeTestContextFilter;
import com.aventstack.extentreports.model.service.ReportStatsService;

public class ReportFilterService {
    public static Report filter(Report report, Set<Status> statusList) {
        Report filtered = Report.builder().build();
        List<Test> list = report.getTestList().stream()
                .filter(x -> statusList.contains(x.getStatus()))
                .collect(Collectors.toList());
        list.forEach(filtered.getTestList()::add);
        ReportStatsService.refreshReportStats(filtered.getStats(), list);
        Set<NamedAttributeTestContext<Author>> authorCtx = new NamedAttributeTestContextFilter<Author>()
                .filter(report.getAuthorCtx(), statusList);
        authorCtx.stream().forEach(x -> filtered.getAuthorCtx().addContext(x.getAttr(), x.getTestList()));
        Set<NamedAttributeTestContext<Category>> categoryCtx = new NamedAttributeTestContextFilter<Category>()
                .filter(report.getCategoryCtx(), statusList);
        categoryCtx.stream().forEach(x -> filtered.getCategoryCtx().addContext(x.getAttr(), x.getTestList()));
        Set<NamedAttributeTestContext<Device>> deviceCtx = new NamedAttributeTestContextFilter<Device>()
                .filter(report.getDeviceCtx(), statusList);
        deviceCtx.stream().forEach(x -> filtered.getDeviceCtx().addContext(x.getAttr(), x.getTestList()));
        return filtered;
    }
}
