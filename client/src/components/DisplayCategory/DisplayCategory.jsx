import './DisplayCategory.css';
import Category from '../Category/Category';

const DisplayCategory = ({ categories, selectedCategory, setSelectedCategory }) => {

    return (
        <div className="row g-3" style={{ width: '100%', margin: 0 }}>

            {categories && categories.map((category) => (

                <div
                    key={category.categoryId}
                    className="col-lg-3 col-md-4 col-sm-6 col-12"
                    style={{ padding: '0 10px' }}
                >

                    <Category
                        categoryName={category.name}
                        imgUrl={category.imgUrl}
                        numberOfItems={category.items}
                        bgColor={category.bgColor}

                        isSelected={selectedCategory === category.categoryId}

                        onClick={() =>
                            setSelectedCategory(
                                selectedCategory === category.categoryId
                                    ? ""
                                    : category.categoryId
                            )
                        }
                    />

                </div>

            ))}

        </div>
    );
};

export default DisplayCategory;