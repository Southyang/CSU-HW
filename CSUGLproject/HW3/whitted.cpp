#include "assignment5.h"

#include "scene_loader.h"

int whitted_main() try {
	srand((uint32_t)time(NULL));

	auto app = csugl::singleton<csugl::Application>::getInstance();
	auto window = &app->GetWindow();
	window->Resize(800, 600);
	auto shader = csugl::Shader::Create("../assets/shader/csucg_assignment5.glsl");
	Quad q;

	const auto scene_path = "../assets/scene/sphere_test/sphere_test.yml";
	//const auto scene_path = "../assets/scene/cornellbox/cornellbox.yml";
	//const auto scene_path = "../assets/scene/veach_mi/veach_mi.yml";
	//const auto scene_path = "../assets/scene/sponza/sponza.yml";
	//const auto scene_path = "../assets/scene/myscene/myscene.yml";

	auto s_loader = csugl::MakeRef<scene_loader>(scene_path);
	window->SetTitle("CSUPBR - " + csugl::fs::get_file_name(scene_path));

	auto film = s_loader->pre_load();

	auto texture = csugl::Texture2D::Create(film->width, film->height, 3);
	
	auto thp = csugl::singleton<csugl::thread_pool>::getInstance(csugl::get_core_numbers());

	csugl::Ref<SceneProperty> scene_p = nullptr;
	csugl::Ref<AsyncRenderer> renderer = nullptr;
	
	bool is_loaded = false;
	auto scene_res = thp->submit([&](){
		scene_p = s_loader->load_scene();
		if (!scene_p) {
			LOG(ERROR) << "Scene load failed";
			app->close();
		}
	});

	csugl::LowpTime::init();
	while (app->isOpen()) {

		if (scene_p && !is_loaded) {
			is_loaded = true;
			LOG(INFO) << "Scene load suc";
			renderer = csugl::MakeRef<AsyncRenderer>(scene_p->film, scene_p->integrater);
		}

		glClearColor(0.3f, 0.5f, 0.6f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		shader->use();
		texture->update_data({ 0, 0 }, { film->width, film->height }, film->get_data());
		shader->set_sampler2D("_tex", 0);
		q.draw();

		window->Display();
		csugl::LowpTime::update();
	}

	csugl::singleton<csugl::Application>::destroy();

	stbi_flip_vertically_on_write(true);
	if (!stbi_write_bmp(("whitted_outimg_" +
						 csugl::fs::get_file_name(scene_path) + ".bmp")
							.c_str(),
						film->width, film->height, 3, film->get_data())) {
		LOG(ERROR) << "Image Write Failed";
	}
	return 0;
} catch (const std::exception& e) {
	LOG(ERROR) << e.what();
	return 0;
}