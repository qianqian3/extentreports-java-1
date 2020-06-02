package com.aventstack.extentreports.model.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.entity.IGherkinFormatterModel;
import com.aventstack.extentreports.gherkin.entity.ScenarioOutline;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;

public class TestService {
    public static Test createTest(Class<? extends IGherkinFormatterModel> type, String name, String description) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Test name cannot be null or empty");
        return Test.builder()
                .bddType(type)
                .name(name)
                .description(description)
                .endTime(Calendar.getInstance().getTime()).build();
    }

    public static Test createTest(String name, String description) {
        return createTest(null, name, description);
    }

    public static Test createTest(String name) {
        return createTest(name, null);
    }

    public static void addNode(Test node, Test parent) {
        if (node == null || parent == null)
            throw new IllegalArgumentException("Parent test or node cannot be null");
        node.setLevel(parent.getLevel() + 1);
        node.setParent(parent);
        node.setLeaf(true);
        parent.setLeaf(false);
        parent.getChildren().add(node);
    }

    public static Test addLog(Log log, Test test) {
        if (test == null || log == null)
            throw new IllegalArgumentException("Test or log must not be null");
        log.setSeq(test.getLogs().size());
        test.getLogs().add(log);
        test.setStatus(Status.max(log.getStatus(), test.getStatus()));
        return test;
    }

    public static boolean deleteTest(List<Test> list, Test test) {
        boolean removed = list.removeIf(x -> x.getId() == test.getId());
        if (!removed)
            list.forEach(x -> TestService.deleteTest(x.getChildren(), test));
        return removed;
    }

    public static Boolean isBDD(Test test) {
        return test != null && test.getBddType() != null;
    }

    public static Boolean testHasLog(Test test) {
        return test != null && !test.getLogs().isEmpty();
    }

    public static Boolean testHasChildren(Test test) {
        return test != null && !test.getChildren().isEmpty();
    }

    public static Boolean testHasAuthor(Test test) {
        return test != null && !test.getAuthorSet().isEmpty();
    }

    public static Boolean testHasCategory(Test test) {
        return test != null && !test.getCategorySet().isEmpty();
    }

    public static Boolean testHasDevice(Test test) {
        return test != null && !test.getDeviceSet().isEmpty();
    }

    public static String fullName(Test test) {
        StringBuilder sb = new StringBuilder(test.getName());
        while (test.getParent() != null) {
            test = test.getParent();
            if (test.getBddType() == null || test.getBddType() != ScenarioOutline.class)
                sb.insert(0, test.getName() + ".");
        }
        return sb.toString();
    }

    public static Boolean testHasAttributes(Test test) {
        return !test.getAuthorSet().isEmpty() ||
                !test.getCategorySet().isEmpty() ||
                !test.getDeviceSet().isEmpty();
    }

    public static Boolean testHasScreenCapture(Test test) {
        return !test.getMedia().isEmpty();
    }

    public static Boolean testHasScreenCapture(Test test, Boolean deep) {
        if (deep) {
            Boolean hasScreenCapture = !test.getMedia().isEmpty()
                    || test.getLogs().stream().anyMatch(LogService::logHasMedia);
            if (!hasScreenCapture)
                hasScreenCapture = test.getChildren().stream().anyMatch(x -> testHasScreenCapture(x, deep));
            return hasScreenCapture;
        }
        return testHasScreenCapture(test);
    }

    public static Long timeTaken(Test test) {
        return test.getEndTime().getTime() - test.getStartTime().getTime();
    }

    public static Status getTestStatus(Test test) {
        computeTestStatus(test);
        return test.getStatus();
    }

    public static List<Test> getLeafList(Test test) {
        List<Test> testList = new ArrayList<>();
        if (test.isLeaf())
            testList.add(test);
        else
            for (Test t : test.getChildren())
                if (t.isLeaf())
                    testList.add(t);
                else
                    testList.addAll(getLeafList(t));
        return testList;
    }

    public static void computeTestStatus(Test test) {
        List<Test> leafList = getLeafList(test);
        TestServiceSupport.computeStatus(leafList);
    }

    private static class TestServiceSupport {
        private static void computeStatus(List<Test> testList) {
            testList.forEach(TestServiceSupport::computeStatus);
        }

        private static void computeStatus(Test t) {
            Set<Status> set = t.getLogs().stream()
                    .map(x -> x.getStatus())
                    .collect(Collectors.toSet());
            set.add(t.getStatus());
            t.setStatus(Status.max(set));
            if (t.getParent() != null) {
                t.getParent().setStatus(Status.max(t.getStatus(), t.getParent().getStatus()));
                computeStatus(t.getParent());
            }
        }
    }
}
