import './Category.css';

const Category = ({
    categoryName,
    imgUrl,
    numberOfItems,
    bgColor,
    isSelected,
    onClick
}) => {

    return (
        <div
            className={`d-flex align-items-center p-3 rounded gap-2 position-relative category-hover ${isSelected ? 'selected-category' : ''}`} style={{ backgroundColor: bgColor, cursor: 'pointer' }}
            onClick={onClick}
        >

            <div style={{ position: 'relative', marginRight: '15px' }}>
                <img
                    src={`http://localhost:8080${imgUrl}`}
                    alt={categoryName}
                    className="category-image"
                />
            </div>

            <div className="category-text">
                <h6 className="text-white mb-0 text-truncate">
                    {categoryName}
                </h6>
                <p className="text-white mb-0 text-truncate">
                    {numberOfItems} Items
                </p>
            </div>



        </div>
    );
};

export default Category;