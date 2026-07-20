import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

function ExportPDF({ tasks }) {

    const exportPDF = () => {

        const doc = new jsPDF();

        doc.setFontSize(18);
        doc.text("KanbanX Task Report", 14, 20);

        autoTable(doc, {
            startY: 30,
            head: [["Title", "Status", "Priority", "Due Date"]],
            body: tasks.map(task => [
                task.title,
                task.status,
                task.priority,
                task.dueDate || "-"
            ])
        });

        doc.save("KanbanX-Tasks.pdf");
    };

    return (
        <button
            className="export-btn"
            onClick={exportPDF}
        >
            📄 Export PDF
        </button>
    );
}

export default ExportPDF;