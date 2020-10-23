/*
 * Autopsy
 *
 * Copyright 2020 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.discovery.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang.StringUtils;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Panel to display list of artifacts for selected domain.
 *
 */
class ArtifactsListPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final DomainArtifactTableModel tableModel = new DomainArtifactTableModel();
    private static final Logger logger = Logger.getLogger(ArtifactsListPanel.class.getName());

    /**
     * Creates new form ArtifactsListPanel.
     */
    ArtifactsListPanel() {
        initComponents();
        jTable1.getRowSorter().toggleSortOrder(0);
        jTable1.getRowSorter().toggleSortOrder(0);
    }

    /**
     * Add a listener to the table of artifacts to perform actions when an
     * artifact is selected.
     *
     * @param listener The listener to add to the table of artifacts.
     */
    void addSelectionListener(ListSelectionListener listener) {
        jTable1.getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Remove a listener from the table of artifacts.
     *
     * @param listener The listener to remove from the table of artifacts.
     */
    void removeListSelectionListener(ListSelectionListener listener) {
        jTable1.getSelectionModel().removeListSelectionListener(listener);
    }

    /**
     * The artifact which is currently selected, null if no artifact is
     * selected.
     *
     * @return The currently selected BlackboardArtifact or null if none is
     *         selected.
     */
    BlackboardArtifact getSelectedArtifact() {
        int selectedIndex = jTable1.convertRowIndexToModel(jTable1.getSelectionModel().getLeadSelectionIndex());
        if (selectedIndex < jTable1.getSelectionModel().getMinSelectionIndex() || jTable1.getSelectionModel().getMaxSelectionIndex() < 0 || selectedIndex > jTable1.getSelectionModel().getMaxSelectionIndex()) {
            return null;
        }
        return tableModel.getArtifactByRow(selectedIndex);
    }

    /**
     * Whether the list of artifacts is empty.
     *
     * @return true if the list of artifacts is empty, false if there are
     *         artifacts.
     */
    boolean isEmpty() {
        return tableModel.getRowCount() <= 0;
    }

    /**
     * Add the specified list of artifacts to the list of artifacts which should
     * be displayed.
     *
     * @param artifactList
     */
    void addArtifacts(List<BlackboardArtifact> artifactList) {
        tableModel.setContents(artifactList);
        jTable1.validate();
        jTable1.repaint();
        tableModel.fireTableDataChanged();
    }

    /**
     * Remove all artifacts from the list of artifacts displayed.
     */
    void clearArtifacts() {
        tableModel.setContents(new ArrayList<>());
        tableModel.fireTableDataChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(tableModel);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Table model which allows the artifact table in this panel to mimic a list
     * of artifacts.
     */
    private class DomainArtifactTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private final List<BlackboardArtifact> artifactList = new ArrayList<>();

        /**
         * Construct a new DomainArtifactTableModel.
         */
        DomainArtifactTableModel() {
            //No arg constructor to create empty model
        }

        /**
         * Set the list of artifacts which should be represented by this table
         * model.
         *
         * @param artifacts The list of BlackboardArtifacts to represent.
         */
        void setContents(List<BlackboardArtifact> artifacts) {
            artifactList.clear();
            artifactList.addAll(artifacts);
        }

        @Override
        public int getRowCount() {
            return artifactList.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        /**
         * Get the BlackboardArtifact at the specified row.
         *
         * @param rowIndex The row the artifact to return is at.
         *
         * @return The BlackboardArtifact at the specified row.
         */
        BlackboardArtifact getArtifactByRow(int rowIndex) {
            return artifactList.get(rowIndex);
        }

        @NbBundle.Messages({"ArtifactsListPanel.value.noValue=No value available."})
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            try {
                int artifactTypeId = getArtifactByRow(rowIndex).getArtifactTypeID();
                if (columnIndex == 1 && (artifactTypeId == BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_CACHE.getTypeID() || artifactTypeId == BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_DOWNLOAD.getTypeID())) {
                    return getArtifactByRow(rowIndex).getParent().getName();
                }
                for (BlackboardAttribute bba : getArtifactByRow(rowIndex).getAttributes()) {
                    if (columnIndex == 0 && bba.getAttributeType().getTypeName().startsWith("TSK_DATETIME_ACCESSED") && !StringUtils.isBlank(bba.getDisplayString())) {
                        return bba.getDisplayString();
                    } else if (columnIndex == 1 && bba.getAttributeType().getTypeName().startsWith("TSK_TITLE") && !StringUtils.isBlank(bba.getDisplayString())) {
                        return bba.getDisplayString();
                    }
                }
                return getFallbackValue(rowIndex, columnIndex);
            } catch (TskCoreException ex) {
                logger.log(Level.WARNING, "Error getting attributes for artifact " + getArtifactByRow(rowIndex).getArtifactID(), ex);
                return Bundle.ArtifactsListPanel_value_noValue();
            }
        }

        /**
         * Private helper method to use when the value we want for either date
         * or title is not available.
         *
         *
         * @param rowIndex    The row the artifact to return is at.
         * @param columnIndex The column index the attribute will be displayed
         *                    at.
         *
         * @return A string that can be used in place of the accessed date time
         *         attribute title when they are not avaiable.
         *
         * @throws TskCoreException
         */
        private String getFallbackValue(int rowIndex, int columnIndex) throws TskCoreException {
            for (BlackboardAttribute bba : getArtifactByRow(rowIndex).getAttributes()) {
                if (columnIndex == 0 && bba.getAttributeType().getTypeName().startsWith("TSK_DATETIME") && !StringUtils.isBlank(bba.getDisplayString())) {
                    return bba.getDisplayString();
                } else if (columnIndex == 1 && bba.getAttributeType().getTypeName().startsWith("TSK_URL") && !StringUtils.isBlank(bba.getDisplayString())) {
                    return bba.getDisplayString();
                }
            }
            return Bundle.ArtifactsListPanel_value_noValue();
        }

        @NbBundle.Messages({"ArtifactsListPanel.titleColumn.name=Title",
            "ArtifactsListPanel.dateColumn.name=Date/Time"})
        @Override
        public String getColumnName(int column
        ) {
            switch (column) {
                case 0:
                    return Bundle.ArtifactsListPanel_dateColumn_name();
                case 1:
                    return Bundle.ArtifactsListPanel_titleColumn_name();
                default:
                    return "";
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
