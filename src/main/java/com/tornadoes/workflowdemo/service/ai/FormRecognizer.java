package com.tornadoes.workflowdemo.service.ai;

import com.azure.ai.formrecognizer.*;

import com.azure.ai.formrecognizer.documentanalysis.models.*;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.polling.SyncPoller;
import com.google.gson.Gson;

import com.tornadoes.workflowdemo.dto.Doc;
import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

public class FormRecognizer {
    //use your `key` and `endpoint` environment variables
    private static final String endpoint = "";
    private static final String key ="";
    private String docUrl;

    public FormRecognizer(String docUrl){
        this.docUrl = docUrl;
    }
    public String docToJson() {

        // create your `DocumentAnalysisClient` instance and `AzureKeyCredential` variable
        DocumentAnalysisClient client = new DocumentAnalysisClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();

        String modelId = "prebuilt-document";

        SyncPoller < OperationResult, AnalyzeResult > analyzeLayoutResultPoller =
                client.beginAnalyzeDocumentFromUrl(modelId, docUrl);

        AnalyzeResult analyzeLayoutResult = analyzeLayoutResultPoller.getFinalResult();

// pages
        analyzeLayoutResult.getPages().forEach(documentPage -> {
            System.out.printf("Page has width: %.2f and height: %.2f, measured with unit: %s%n",
                    documentPage.getWidth(),
                    documentPage.getHeight(),
                    documentPage.getUnit());

            // selection marks
            documentPage.getSelectionMarks().forEach(documentSelectionMark ->
                    System.out.printf("Selection mark is '%s' and is within a bounding polygon %s with confidence %.2f.%n",
                            documentSelectionMark.getSelectionMarkState().toString(),
                            getBoundingCoordinates(documentSelectionMark.getBoundingPolygon()),
                            documentSelectionMark.getConfidence()));
        });

// tables
        List < DocumentTable > tables = analyzeLayoutResult.getTables();
        for (int i = 0; i < tables.size(); i++) {
            DocumentTable documentTables = tables.get(i);
            System.out.printf("Table %d has %d rows and %d columns.%n", i, documentTables.getRowCount(),
                    documentTables.getColumnCount());
            documentTables.getCells().forEach(documentTableCell -> {
                System.out.printf("Cell '%s', has row index %d and column index %d.%n", documentTableCell.getContent(),
                        documentTableCell.getRowIndex(), documentTableCell.getColumnIndex());
            });
            System.out.println();
        }


        List<Doc> list = new ArrayList<Doc>();
// Key-value pairs
        analyzeLayoutResult.getKeyValuePairs().forEach(documentKeyValuePair -> {
            Doc doc = new Doc();
            System.out.printf("Key content: %s%n", documentKeyValuePair.getKey().getContent());

            doc.setKey(documentKeyValuePair.getKey().getContent());
            if (documentKeyValuePair.getValue() != null) {
                System.out.printf("Value content: %s%n", documentKeyValuePair.getValue().getContent());
                doc.setValue(documentKeyValuePair.getValue().getContent());
            }
            list.add(doc);

        });

        return new Gson().toJson(list);
    }

    // Utility function to get the bounding polygon coordinates.
    private static String getBoundingCoordinates(List < Point > boundingPolygon) {
        return boundingPolygon.stream().map(point -> String.format("[%.2f, %.2f]", point.getX(),
                point.getY())).collect(Collectors.joining(", "));
    }

}
