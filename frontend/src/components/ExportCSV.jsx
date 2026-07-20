import { saveAs } from "file-saver";

function ExportCSV({ tasks }) {

    const exportCSV = () => {

        const headers = [
            "Title",
            "Status",
            "Priority",
            "Due Date"
        ];

        const rows = tasks.map(task => [
            task.title,
            task.status,
            task.priority,
            task.dueDate || "-"
        ]);

        const csv =
            [headers, ...rows]
                .map(row => row.join(","))
                .join("\n");

        const blob = new Blob(
            [csv],
            { type: "text/csv;charset=utf-8;" }
        );

        saveAs(blob, "KanbanX-Tasks.csv");
    };

    return (
        <button
            className="export-btn"
            onClick={exportCSV}
        >
            📊 Export CSV
        </button>
    );
}

export default ExportCSV;